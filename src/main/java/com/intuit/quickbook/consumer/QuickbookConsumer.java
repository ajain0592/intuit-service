package com.intuit.quickbook.consumer;

import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.quickbook.service.QBValidateProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class QuickbookConsumer {

    private static final Logger logger = LoggerFactory.getLogger(QuickbookConsumer.class);

    @Value("${quickbook.productId}")
    private int productId;

    @Autowired
    QBValidateProfile qbValidateProfile;

    /**
     * Consumes ProfileValidationDTO from profile_validation_topic
     * @return void
     */
    @KafkaListener(topics = "profile_validation_topic", containerFactory = "qbKafkaListenerContainerFactory")
    public void listen(ProfileValidationDTO record) {
        logger.info("Quick Book Validation Consumer : Received message {}" ,record);

        if(!record.getProductIds().contains(productId)) {
            logger.info("User {} has not subscribed to Quick Books. No need to validate data", record.getUserId());
            return;
        }

        qbValidateProfile.process(record);
    }
}
