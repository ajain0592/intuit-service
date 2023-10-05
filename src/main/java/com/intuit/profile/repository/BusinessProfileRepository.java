package com.intuit.profile.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.intuit.profile.entity.BusinessProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessProfileRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Save BusinessProfile to DB
     * @return BusinessProfile
     */
    public BusinessProfile save(BusinessProfile businessProfile) {
        dynamoDBMapper.save(businessProfile);
        return businessProfile;
    }

    /**
     * Get BusinessProfile of user id and company name
     * @return BusinessProfile
     */
    public BusinessProfile getBusinessProfileByUserIdAndCompanyName(String userId, String companyName) {
        return dynamoDBMapper.load(BusinessProfile.class, userId, companyName);
    }

}
