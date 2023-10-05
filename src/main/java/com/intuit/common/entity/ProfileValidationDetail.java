package com.intuit.common.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.intuit.common.constant.RequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "profile_update_validation_detail")
public class ProfileValidationDetail {

    @DynamoDBHashKey
    private String requestId;

    @DynamoDBRangeKey
    private String productId;

    private RequestStatusEnum status;

    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return status.toString();
    }

    public void setStatus(String status) {
        this.status = RequestStatusEnum.valueOf(status); // Convert string to enum when reading from DynamoDB
    }



}
