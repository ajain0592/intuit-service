package com.intuit.monitor.consumer;

import com.intuit.common.entity.MonitorDTO;
import com.intuit.monitor.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MonitorConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MonitorConsumer.class);

    @Autowired
    private MonitorService monitorService;

    /**
     * Consumes MonitorDTO from monitor_topic
     * @return void
     */
    @KafkaListener(topics = "monitor_topic", containerFactory = "monitorKafkaListenerContainerFactory")
    public void listen(MonitorDTO record) {
        logger.info("Monitor Consumer : Received message {}" ,record);
        monitorService.monitorService(record);
    }



}
