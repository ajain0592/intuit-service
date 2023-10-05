package com.intuit.common.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.intuit.common.entity.ProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileUpdateRequestRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Save ProfileUpdateRequest to DB
     * @return ProfileUpdateRequest
     */
    public ProfileUpdateRequest save(ProfileUpdateRequest businessProfileUpdateRequest) {
        dynamoDBMapper.save(businessProfileUpdateRequest);
        return businessProfileUpdateRequest;
    }

    /**
     * Get ProfileUpdateRequest of user Id and request Id
     * @return ProfileUpdateRequest
     */
    public ProfileUpdateRequest getProfileUpdateStatus(String userId, String requestId) {
        return dynamoDBMapper.load(ProfileUpdateRequest.class, userId, requestId);
    }

    /**
     * Get All ProfileUpdateRequest status of user Id
     * @return List<ProfileUpdateRequest>
     */
    public List<ProfileUpdateRequest> getAllProfileUpdateRequestStatus(String userId) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition(
                "userId",
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ) // Equality comparison
                        .withAttributeValueList(new AttributeValue().withS(userId)) // Desired primary key value
        );
        return dynamoDBMapper.scan(ProfileUpdateRequest.class, scanExpression);
    }
}
