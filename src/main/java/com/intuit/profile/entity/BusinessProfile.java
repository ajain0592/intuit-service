package com.intuit.profile.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "business_profile")
public class BusinessProfile {

    @DynamoDBHashKey
    private String userId;

    @DynamoDBRangeKey
    private String companyName;

    @DynamoDBAttribute
    private String legalName;

    @DynamoDBAttribute
    private BusinessAddress businessAddress;

    @DynamoDBAttribute
    private String legalAddress;

    @DynamoDBAttribute
    private TaxIdentifiers taxIdentifiers;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String webSite;

}
