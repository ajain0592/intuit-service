package com.intuit.tsheets.consumer;

import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.tsheets.service.TSheetValidateProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TSheetsConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TSheetsConsumer.class);

    @Value("${tSheet.productId}")
    private int productId;

    @Autowired
    TSheetValidateProfile tSheetValidateProfile;

    @KafkaListener(topics = "profile_validation_topic", containerFactory = "tSheetKafkaListenerContainerFactory")
    public void listen(ProfileValidationDTO record) {
        logger.info("TSheets Validation Consumer : Received message {}" ,record);

        if(!record.getProductIds().contains(productId)) {
            logger.info("User {} has not subscribed to TSheets. No need to validate data", record.getUserId());
            return;
        }

        tSheetValidateProfile.process(record);
    }
}
