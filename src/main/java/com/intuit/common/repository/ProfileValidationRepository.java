package com.intuit.common.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.intuit.common.entity.ProfileValidationDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileValidationRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Save ProfileValidationDetail to DB
     * @return ProfileValidationDetail
     */
    public ProfileValidationDetail save(ProfileValidationDetail profileValidationDetail) {
        dynamoDBMapper.save(profileValidationDetail);
        return profileValidationDetail;
    }

    /**
     * Get ProfileValidationDetail of request Id and product Id
     * @return ProfileValidationDetail
     */
    public ProfileValidationDetail getProfileValidationByRequestIdAndProductId(String requestId, String productId ) {
        return dynamoDBMapper.load(ProfileValidationDetail.class, requestId, productId);
    }

    /**
     * Get All ProfileValidationDetail of request Id
     * @return List<ProfileValidationDetail>
     */
    public List<ProfileValidationDetail> getProfileValidationsByRequestId(String requestId) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition(
                "requestId", // Attribute name
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ) // Equality comparison
                        .withAttributeValueList(new AttributeValue().withS(requestId)) // Desired primary key value
        );
        return dynamoDBMapper.scan(ProfileValidationDetail.class, scanExpression);
    }
}
