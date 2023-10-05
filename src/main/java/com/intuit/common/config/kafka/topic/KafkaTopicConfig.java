package com.intuit.common.config.kafka.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig
{
    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value("${kafka.profileValidationTopic}")
    private String profileValidationTopic;

    @Value("${kafka.monitorTopic}")
    private String monitorTopic;

    /**
     * Creates profileValidationTopic
     * @return Topic
     */
    @Bean
    public NewTopic profileValidationTopic() {
        return TopicBuilder.name(profileValidationTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    /**
     * Creates monitorTopic
     * @return Topic
     */
    @Bean
    public NewTopic monitorTopic() {
        return TopicBuilder.name(monitorTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}