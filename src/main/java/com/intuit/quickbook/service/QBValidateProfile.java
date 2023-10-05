package com.intuit.quickbook.service;

import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.profile.entity.BusinessProfile;
import org.springframework.stereotype.Service;

@Service
public interface QBValidateProfile {

    void process(ProfileValidationDTO profileValidationDTO);

    boolean validateProfile(BusinessProfile businessProfile);
}
