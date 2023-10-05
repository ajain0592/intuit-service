package com.intuit.quickbook.service.impl;

import com.intuit.common.config.kafka.producer.KafkaProducerService;
import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.common.entity.MonitorDTO;
import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.common.entity.ProfileValidationDetail;
import com.intuit.common.repository.ProfileValidationRepository;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.quickbook.service.QBValidateProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QBValidateProfileImpl implements QBValidateProfile {

    private static final Logger logger = LoggerFactory.getLogger(QBValidateProfileImpl.class);

    @Value("${quickbook.productId}")
    private int productId;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    ProfileValidationRepository profileValidationRepository;

    /**
     * Process ProfileValidationDTO, validates it and updates Profile Validation Detail table
     * and produce MonitorDTO  on monitor_topic
     * @return void
     */
    @Override
    public void process(ProfileValidationDTO profileValidationDTO) {
        ProfileValidationDetail profileValidationDetail = ProfileValidationDetail.builder()
                .requestId(profileValidationDTO.getRequestId())
                .productId(String.valueOf(productId))
                .status(RequestStatusEnum.INPROGRESS)
                .build();

        profileValidationRepository.save(profileValidationDetail);

        logger.info("Quick Book Validation Consumer : " +
                "Setting the validation request to INPROGRESS for requestId {}", profileValidationDTO.getRequestId());

        if (this.validateProfile(profileValidationDTO.getBusinessProfile())) {
            logger.info("Quick Book Validation Consumer : " +
                    "Successfully validated the data for request Id {}", profileValidationDTO.getRequestId());

            profileValidationDetail.setStatus(RequestStatusEnum.APPROVED.toString());
            profileValidationRepository.save(profileValidationDetail);

            logger.info("Quick Book Validation Consumer : " +
                    "Updated the validation status to APPROVED for request id {}", profileValidationDTO.getRequestId());

            //Put to Monitor Kafka Topic
            MonitorDTO monitorDTO = MonitorDTO.builder()
                    .userId(profileValidationDTO.getUserId())
                    .requestId(profileValidationDTO.getRequestId())
                    .productId(String.valueOf(productId))
                    .status(RequestStatusEnum.APPROVED)
                    .businessProfile(profileValidationDTO.getBusinessProfile())
                    .build();

            kafkaProducerService.sendMonitorEvent(monitorDTO);
            logger.info("Quick Book Validation Consumer : " +
                    "Sent monitor event to Monitor Topic for requestId {}", profileValidationDTO.getRequestId());
        } else {
            logger.info("Quick Book Validation Consumer : " +
                    "Validation rejected for request Id {}", profileValidationDTO.getRequestId());

            profileValidationDetail.setStatus(RequestStatusEnum.REJECTED.toString());
            profileValidationRepository.save(profileValidationDetail);

            logger.info("Quick Book Validation Consumer : " +
                    "Updated the validation status to REJECTED for request id {}", profileValidationDTO.getRequestId());

            //Put to Monitor Kafka Topic
            MonitorDTO monitorDTO = MonitorDTO.builder()
                    .userId(profileValidationDTO.getUserId())
                    .requestId(profileValidationDTO.getRequestId())
                    .productId(String.valueOf(productId))
                    .status(RequestStatusEnum.REJECTED)
                    .businessProfile(profileValidationDTO.getBusinessProfile())
                    .build();

            kafkaProducerService.sendMonitorEvent(monitorDTO);
            logger.info("Quick Book Validation Consumer : " +
                    "Sent monitor event to Monitor Topic for requestId {}", profileValidationDTO.getRequestId());
        }
    }

    /**
     * validates Profile
     * @return boolean
     */
    @Override
    public boolean validateProfile(BusinessProfile businessProfile) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
