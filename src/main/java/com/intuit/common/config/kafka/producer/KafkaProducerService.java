package com.intuit.common.config.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.common.entity.MonitorDTO;
import com.intuit.common.entity.ProfileValidationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${kafka.profileValidationTopic}")
    private String profileValidationTopic;

    @Value("${kafka.monitorTopic}")
    private String monitorTopic;

    @Autowired
    private KafkaTemplate<String, ProfileValidationDTO> profileDTOKafkaTemplate;

    @Autowired
    private KafkaTemplate<String, MonitorDTO> monitorDTOKafkaTemplate;


    /**
     * Produces ProfileValidationDTO on profileValidationTopic
     * @return void
     */
    public void sendProfileUpdateEvent(ProfileValidationDTO profileValidationDTO) throws JsonProcessingException {
        ListenableFuture<SendResult<String, ProfileValidationDTO>> future
                = this.profileDTOKafkaTemplate.send(profileValidationTopic, profileValidationDTO);

        future.addCallback(new ListenableFutureCallback<SendResult<String, ProfileValidationDTO>>() {
            @Override
            public void onSuccess(SendResult<String, ProfileValidationDTO> result) {
                logger.info("Successfully published the ProfileValidationDTO {} to {}", profileValidationDTO, profileValidationTopic);
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("Failed to publish the ProfileValidationDTO {} to {} exception received {}",
                        profileValidationDTO, profileValidationTopic, ex.getMessage());
            }
        });
    }

    /**
     * Produces MonitorDTO on monitorTopic
     * @return void
     */
    public void sendMonitorEvent(MonitorDTO monitorDTO)  {
        ListenableFuture<SendResult<String, MonitorDTO>> future
                = this.monitorDTOKafkaTemplate.send(monitorTopic, monitorDTO);

        future.addCallback(new ListenableFutureCallback<SendResult<String, MonitorDTO>>() {
            @Override
            public void onSuccess(SendResult<String, MonitorDTO> result) {
                logger.info("Successfully published the MonitorDTO {} to {}", monitorDTO, monitorTopic);
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("Failed to publish the MonitorDTO {} to {} exception received {}"
                        , monitorDTO, monitorTopic, ex.getMessage());
            }
        });
    }


}
