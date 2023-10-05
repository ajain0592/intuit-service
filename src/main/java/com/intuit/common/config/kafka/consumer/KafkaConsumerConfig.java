package com.intuit.common.config.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import com.intuit.common.entity.MonitorDTO;
import com.intuit.common.entity.ProfileValidationDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig
{
    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value("${kafka.quickbook.consumer.groupId}")
    private String qbConsumerGroupId;

    @Value("${kafka.tSheet.consumer.groupId}")
    private String tSheetConsumerGroupId;

    @Value("${kafka.monitor.consumer.groupId}")
    private String monitorConsumerGroupId;

    /**
     * Creates ConsumerFactory for consuming ProfileDTO for Quick books
     * @return ConsumerFactory<String, ProfileDTO>
     */
    public ConsumerFactory<String, ProfileValidationDTO> qbConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, qbConsumerGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(ProfileValidationDTO.class));
    }

    /**
     * Creates ContainerFactory for consuming ProfileDTO for Quick Books Consumer
     * @return ContainerFactory<String, ProfileDTO>
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileValidationDTO> qbKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProfileValidationDTO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(qbConsumerFactory());
        return factory;
    }

    /**
     * Creates ConsumerFactory for consuming ProfileDTO for tsheets
     * @return ConsumerFactory<String, ProfileDTO>
     */
    public ConsumerFactory<String, ProfileValidationDTO> tSheetConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, tSheetConsumerGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(ProfileValidationDTO.class));
    }

    /**
     * Creates ContainerFactory for consuming ProfileDTO for TSheet Consumer
     * @return ContainerFactory<String, ProfileDTO>
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProfileValidationDTO> tSheetKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProfileValidationDTO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(tSheetConsumerFactory());
        return factory;
    }

    /**
     * Creates ConsumerFactory for consuming MonitorDTO for Monitor Consumer
     * @return ConsumerFactory<String, MonitorDTO>
     */
    public ConsumerFactory<String, MonitorDTO> monitorConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, monitorConsumerGroupId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(MonitorDTO.class));
    }

    /**
     * Creates ContainerFactory for consuming MonitorDTO for Monitor Consumer
     * @return ContainerFactory<String, MonitorDTO>
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MonitorDTO> monitorKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MonitorDTO> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(monitorConsumerFactory());
        return factory;
    }
}