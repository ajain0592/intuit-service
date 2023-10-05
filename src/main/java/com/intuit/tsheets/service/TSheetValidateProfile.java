package com.intuit.tsheets.service;

import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.profile.entity.BusinessProfile;
import org.springframework.stereotype.Service;

@Service
public interface TSheetValidateProfile {

    void process(ProfileValidationDTO profileValidationDTO);

    boolean validateProfile(BusinessProfile businessProfile);
}
