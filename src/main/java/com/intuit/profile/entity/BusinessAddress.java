package com.intuit.profile.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamoDBDocument
public class BusinessAddress {

    private String line1;

    private String line2;

    private String city;

    private String state;

    private String zip;

    private String country;
}
