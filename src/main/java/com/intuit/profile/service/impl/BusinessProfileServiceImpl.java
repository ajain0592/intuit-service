package com.intuit.profile.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.common.entity.ProfileUpdateRequest;
import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.common.entity.ProfileValidationDetail;
import com.intuit.common.entity.UserProductMapping;
import com.intuit.common.repository.ProfileUpdateRequestRepository;
import com.intuit.common.repository.ProfileValidationRepository;
import com.intuit.common.repository.UserProductMappingRepository;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.exception.UserNotSubscribedToProductsException;
import com.intuit.profile.repository.BusinessProfileRepository;
import com.intuit.profile.service.BusinessProfileService;
import com.intuit.common.config.kafka.producer.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BusinessProfileServiceImpl implements BusinessProfileService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessProfileServiceImpl.class);

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    @Autowired
    private ProfileUpdateRequestRepository profileUpdateRequestRepository;

    @Autowired
    private ProfileValidationRepository profileValidationRepository;

    @Autowired
    private UserProductMappingRepository userProductMappingRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    /**
     * Generate request Id and insert entry into profile Update request table
     * and send Profile Validation DTO async
     * @return String
     */
    @Override
    public String acceptRequest(String userId, BusinessProfile businessProfile){
        String requestId = UUID.randomUUID().toString();

        //Update in DB and produces message on price_update_topic
        CompletableFuture.runAsync(() -> {
            updateAndProduce(userId, requestId, businessProfile);
        });

        return requestId;
    }

    /**
     * Insert entry into profile Update request table
     * and send Profile Validation DTO async
     * @return void
     */
    public void updateAndProduce(String userId, String requestId, BusinessProfile businessProfile) {

        ProfileUpdateRequest updateRequest = ProfileUpdateRequest.builder()
                .userId(userId)
                .requestId(requestId)
                .status(RequestStatusEnum.ACCEPTED)
                .build();

        profileUpdateRequestRepository.save(updateRequest);

        logger.info("Entry Inserted for userId {} and request id {} " +
                "in business_profile_update_request table", userId, requestId);

        List<String> productIds = this.getSubscribedProducts(userId);

        logger.info("Subscribed products for userId {} is {} ", userId, productIds);

        if(productIds.isEmpty()) {
            logger.error("User {} is not subscribed to any products. Cant update Business Profile", userId);
            throw new UserNotSubscribedToProductsException("User is not subscribed to any products. " +
                    "Cant update Business Profile");
        }

        logger.info("Subscribed products for the userId is {} ", productIds);

        //Insert into profile_validation table
        for (String productId : productIds) {
            ProfileValidationDetail profileValidationDetail = ProfileValidationDetail.builder()
                    .requestId(requestId)
                    .productId(productId)
                    .status(RequestStatusEnum.ACCEPTED)
                    .build();

            profileValidationRepository.save(profileValidationDetail);

            logger.info("Entry Inserted for request id {} and product id {} " +
                    "in profile_update_validation_detail table", requestId, productId);
        }

        //Produce message to kafka topic
        ProfileValidationDTO profileValidationDTO = ProfileValidationDTO
                .builder()
                .userId(userId)
                .requestId(requestId)
                .businessProfile(businessProfile)
                .productIds(Arrays.asList(1,2))
                .build();

        try {
            updateRequest.setStatus(RequestStatusEnum.INPROGRESS.toString());

            kafkaProducerService.sendProfileUpdateEvent(profileValidationDTO);
            logger.info("Successfully published the message to profile_update_topic");

            profileUpdateRequestRepository.save(updateRequest);

        } catch (JsonProcessingException e) {
            logger.error("Failed to publish message to profile_update topic. Exception received", e);
        }

    }

    /**
     * Retrieves BusinessProfile for user Id and company Name
     * @return BusinessProfile
     */
    public BusinessProfile getBusinessProfile(String userId, String companyName){
        return businessProfileRepository.getBusinessProfileByUserIdAndCompanyName(userId, companyName);
    }

    /**
     * Retrieves status of request id and user id
     * @return ProfileUpdateRequest
     */
    public ProfileUpdateRequest getStatus(String userId, String requestId){
        return profileUpdateRequestRepository.getProfileUpdateStatus(userId, requestId);
    }

    /**
     * Retrieves status of All request for user id
     * @return List<ProfileUpdateRequest>
     */
    public List<ProfileUpdateRequest> getAllRequestStatus(String userId) {
        return profileUpdateRequestRepository.getAllProfileUpdateRequestStatus(userId);
    }

    @Cacheable(value = "userProductCache", key = "userId")
    public List<String> getSubscribedProducts(String userId) {
        List<UserProductMapping> userProductMappings =
                userProductMappingRepository.getUserProductMapping(userId);

        return userProductMappings.stream().map(userProductMapping ->
                userProductMapping.getProductId()).collect(Collectors.toList());
    }
}
