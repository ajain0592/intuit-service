package com.intuit.common.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.intuit.common.entity.ProfileValidationDetail;
import com.intuit.common.entity.UserProductMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProductMappingRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Get All User Product Mapping of user Id
     * @return List<ProfileValidationDetail>
     */
    public List<UserProductMapping> getUserProductMapping(String userId) {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        scanExpression.addFilterCondition(
                "userId", // Attribute name
                new Condition()
                        .withComparisonOperator(ComparisonOperator.EQ) // Equality comparison
                        .withAttributeValueList(new AttributeValue().withS(userId)) // Desired primary key value
        );
        return dynamoDBMapper.scan(UserProductMapping.class, scanExpression);
    }
}
