package com.intuit.common.config.kafka.producer;

import java.util.HashMap;
import java.util.Map;

import com.intuit.common.entity.MonitorDTO;
import com.intuit.common.entity.ProfileValidationDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig
{
    @Value("${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    /**
     * Creates ProducerFactory for producing ProfileValidationDTO
     * @return ProducerFactory<String, ProfileValidationDTO>
     */
    @Bean
    public ProducerFactory<String, ProfileValidationDTO> userProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates KafkaTemplate for producing ProfileValidationDTO
     * @return KafkaTemplate<String, ProfileValidationDTO>
     */
    @Bean
    public KafkaTemplate<String, ProfileValidationDTO> userKafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }

    /**
     * Creates ProducerFactory for producing MonitorDTO
     * @return ProducerFactory<String, MonitorDTO>
     */
    @Bean
    public ProducerFactory<String, MonitorDTO> monitorDTOProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates KafkaTemplate for producing MonitorDTO
     * @return KafkaTemplate<String, MonitorDTO>
     */
    @Bean
    public KafkaTemplate<String, MonitorDTO> monitorDTOKafkaTemplate() {
        return new KafkaTemplate<>(monitorDTOProducerFactory());
    }
}