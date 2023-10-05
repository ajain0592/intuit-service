package com.intuit.profile.service;

import com.intuit.common.entity.ProfileUpdateRequest;
import com.intuit.profile.entity.BusinessProfile;

import java.util.List;

public interface BusinessProfileService {

    String acceptRequest(String userId, BusinessProfile businessProfile);

    BusinessProfile getBusinessProfile(String userId, String companyName);

    ProfileUpdateRequest getStatus(String userId, String requestId);

    List<ProfileUpdateRequest> getAllRequestStatus(String userId);
}
