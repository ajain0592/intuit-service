package com.intuit.monitor.service.impl;

import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.common.entity.MonitorDTO;
import com.intuit.common.entity.ProfileUpdateRequest;
import com.intuit.common.entity.ProfileValidationDetail;
import com.intuit.common.repository.ProfileUpdateRequestRepository;
import com.intuit.common.repository.ProfileValidationRepository;
import com.intuit.monitor.service.MonitorService;
import com.intuit.profile.repository.BusinessProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorServiceImpl implements MonitorService {

    private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);

    @Autowired
    private ProfileValidationRepository profileValidationRepository;

    @Autowired
    private ProfileUpdateRequestRepository profileUpdateRequestRepository;

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    /**
     * Process MonitorDTO and updates Profile Update and Business Profile table
     * based on status received from Products
     * @return void
     */
    @Override
    public void monitorService(MonitorDTO monitorDTO) {

        boolean updateProfile = true;

        if (monitorDTO.getStatus() == RequestStatusEnum.REJECTED) {
            logger.info("Monitor Consumer : REJECT request received for request id {} from product id {} " ,
                    monitorDTO.getRequestId(), monitorDTO.getProductId());

            ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                    .userId(monitorDTO.getUserId())
                    .requestId(monitorDTO.getRequestId())
                    .status(RequestStatusEnum.REJECTED)
                    .build();

            profileUpdateRequestRepository.save(profileUpdateRequest);

            logger.info("Monitor Consumer : Updating the status to REJECTED " +
                    "for request id {} in  business_profile_update_request" , monitorDTO.getRequestId());

        } else if (monitorDTO.getStatus() == RequestStatusEnum.APPROVED) {
            //Check for all the products
            logger.info("Monitor Consumer : APPROVED request received for request id {} from product id {} " ,
                    monitorDTO.getRequestId(), monitorDTO.getProductId());

            List<ProfileValidationDetail> profileValidationDetails =
                    profileValidationRepository.getProfileValidationsByRequestId(monitorDTO.getRequestId());

            for (ProfileValidationDetail profileValidationDetail : profileValidationDetails) {
                if (RequestStatusEnum.valueOf(profileValidationDetail.getStatus()) != RequestStatusEnum.APPROVED ) {
                    updateProfile = false;
                }
            }

            if(updateProfile) {
                logger.info("Monitor Consumer : APPROVED request received from all subscribed products" +
                        "for request id {}" , monitorDTO.getRequestId());

                ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                        .userId(monitorDTO.getUserId())
                        .requestId(monitorDTO.getRequestId())
                        .status(RequestStatusEnum.APPROVED)
                        .build();

                profileUpdateRequestRepository.save(profileUpdateRequest);

                logger.info("Monitor Consumer : Updating the status to APPROVED for request Id {}", monitorDTO.getRequestId());

                businessProfileRepository.save(monitorDTO.getBusinessProfile());

                logger.info("Monitor Consumer : Successfully updated the business profile for request Id {}",
                        monitorDTO.getRequestId());
            }
        }
    }
}
