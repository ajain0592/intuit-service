package com.intuit.profile.controller;

import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.common.entity.ProfileUpdateRequest;
import com.intuit.common.entity.ProfileUpdateResponse;
import com.intuit.profile.entity.BusinessAddress;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.entity.TaxIdentifiers;
import com.intuit.profile.service.BusinessProfileService;
import com.intuit.profile.service.ValidateBusinessProfileService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class BusinessProfileControllerTest {

    @InjectMocks
    private BusinessProfileController businessProfileController;

    @Mock
    private BusinessProfileService businessProfileService;

    @Mock
    private ValidateBusinessProfileService validateBusinessProfileService;

    BusinessProfile businessProfile;

    @BeforeEach
    public void setUp() {

        BusinessAddress businessAddress = BusinessAddress.builder()
                .line1("Line 1")
                .line2("Line 2")
                .city("Banglore")
                .state("Karnataka")
                .country("India")
                .zip("560066")
                .build();

        TaxIdentifiers taxIdentifiers = TaxIdentifiers.builder()
                .ein("ABCD1234")
                .pan("ABCD1234")
                .build();

        businessProfile = BusinessProfile
                .builder()
                .userId("1")
                .companyName("Company Name")
                .legalName("Legal Name")
                .businessAddress(businessAddress)
                .legalAddress("Legal Address")
                .email("test@test.com")
                .taxIdentifiers(taxIdentifiers)
                .webSite("https://test.com")
                .build();
    }

    @Test
    public void acceptRequestTest() {
        String userId = "1";
        Mockito.when(businessProfileService.acceptRequest(userId, businessProfile)).
                thenReturn("a5b70d98-fcd3-454c-8200-c4d90d225e81");

        Mockito.when(validateBusinessProfileService.validate(businessProfile)).
                thenReturn(true);

        ResponseEntity<ProfileUpdateResponse> responseEntity = businessProfileController.
                acceptRequest(userId, businessProfile);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("a5b70d98-fcd3-454c-8200-c4d90d225e81", responseEntity.getBody().getRequestId());
    }

    @Test
    public void getBusinessProfileTest() {
        String userId = "1";
        String companyName = "Test";

        Mockito.when(businessProfileService.getBusinessProfile(userId, companyName)).
                thenReturn(businessProfile);

        ResponseEntity<BusinessProfile> responseEntity = businessProfileController.
                getBusinessProfile(userId, companyName);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(businessProfile, responseEntity.getBody());
    }

    @Test
    public void getStatusTest() {
        String userId = "1";
        String requestId = "a5b70d98-fcd3-454c-8200-c4d90d225e81";

        ProfileUpdateRequest profileUpdateRequest = ProfileUpdateRequest.builder()
                .requestId(requestId)
                .userId(userId)
                .status(RequestStatusEnum.INPROGRESS)
                .build();

        Mockito.when(businessProfileService.getStatus(userId, requestId)).
                thenReturn(profileUpdateRequest);

        ResponseEntity<ProfileUpdateRequest> responseEntity = businessProfileController.
                getStatus(userId, requestId);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(profileUpdateRequest, responseEntity.getBody());
    }


}
