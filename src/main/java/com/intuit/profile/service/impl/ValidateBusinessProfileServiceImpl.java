package com.intuit.profile.service.impl;

import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.exception.BusinessProfileNotValidException;
import com.intuit.profile.service.ValidateBusinessProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class ValidateBusinessProfileServiceImpl implements ValidateBusinessProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ValidateBusinessProfileServiceImpl.class);

    /**
     * Validate if Business Profile is valid or not
     * @return boolean
     * @throws BusinessProfileNotValidException
     */
    @Override
    public boolean validate(BusinessProfile businessProfile) throws BusinessProfileNotValidException{

        if (Objects.isNull(businessProfile.getCompanyName()) ||
            Objects.isNull(businessProfile.getLegalName()) ||
            Objects.isNull(businessProfile.getBusinessAddress().getLine1()) ||
            Objects.isNull(businessProfile.getBusinessAddress().getCity()) ||
            Objects.isNull(businessProfile.getBusinessAddress().getState()) ||
            Objects.isNull(businessProfile.getBusinessAddress().getZip()) ||
            Objects.isNull(businessProfile.getBusinessAddress().getCountry()) ||
            Objects.isNull(businessProfile.getLegalAddress()) ||
            Objects.isNull(businessProfile.getTaxIdentifiers().getPan()) ||
            Objects.isNull(businessProfile.getTaxIdentifiers().getEin()) ||
            Objects.isNull(businessProfile.getEmail())) {

            throw new BusinessProfileNotValidException("Business Profile is not Valid");
        }
        return true;
    }

}
