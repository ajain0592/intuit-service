package com.intuit.common.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.intuit.common.constant.RequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "business_profile_update_request")
public class ProfileUpdateRequest {

    @DynamoDBHashKey
    private String userId;

    @DynamoDBRangeKey
    private String requestId;

    private RequestStatusEnum status;

    @DynamoDBAttribute(attributeName = "status")
    public String getStatus() {
        return status.toString();
    }

    public void setStatus(String status) {
        this.status = RequestStatusEnum.valueOf(status); // Convert string to enum when reading from DynamoDB
    }



}
