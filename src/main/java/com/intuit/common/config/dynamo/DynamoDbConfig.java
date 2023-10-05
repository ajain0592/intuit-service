package com.intuit.common.config.dynamo;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

    @Value("${dynamodb.serviceEndPoint}")
    private String serviceEndPoint;

    @Value("${dynamodb.signingRegion}")
    private String signingRegion;

    @Value("${dynamodb.accessKey}")
    private String accessKey;

    @Value("${dynamodb.secretKey}")
    private String secretKey;

    /**
     * Configs DynamoDbMapper used for making connection with Dynamo DB
     * @return DynamoDBMapper
     */
    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(buildAmazonDynamoDB());
    }

    /**
     * Configures connection with Amazon Dynamo DB
     * @return AmazonDynamoDB
     */
    private AmazonDynamoDB buildAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        serviceEndPoint, signingRegion
                    )
                )
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        accessKey, secretKey
                                )
                        )
                ).build();
    }
}
