package com.intuit.profile.service;

import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.exception.BusinessProfileNotValidException;


public interface ValidateBusinessProfileService {

    boolean validate(BusinessProfile businessProfile) throws BusinessProfileNotValidException;
}
