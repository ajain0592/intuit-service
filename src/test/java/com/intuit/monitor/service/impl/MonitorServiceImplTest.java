package com.intuit.monitor.service.impl;

import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.common.entity.MonitorDTO;
import com.intuit.common.entity.ProfileUpdateRequest;
import com.intuit.common.entity.ProfileValidationDetail;
import com.intuit.common.repository.ProfileUpdateRequestRepository;
import com.intuit.common.repository.ProfileValidationRepository;
import com.intuit.monitor.consumer.MonitorConsumer;
import com.intuit.monitor.service.MonitorService;
import com.intuit.profile.entity.BusinessAddress;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.entity.TaxIdentifiers;
import com.intuit.profile.repository.BusinessProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class MonitorServiceImplTest {

    MonitorDTO monitorDTO;

    BusinessProfile businessProfile;

    @InjectMocks
    MonitorServiceImpl monitorService;

    @Mock
    ProfileUpdateRequestRepository profileUpdateRequestRepository;

    @Mock
    private ProfileValidationRepository profileValidationRepository;

    @Mock
    private BusinessProfileRepository businessProfileRepository;

    ProfileUpdateRequest profileUpdateRequest;

    ProfileValidationDetail profileValidationDetail;

    private final String userId = "1";

    private final String requestId = "a5b70d98-fcd3-454c-8200-c4d90d225e81";


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

        monitorDTO = MonitorDTO.builder()
                .userId(userId)
                .businessProfile(businessProfile)
                .status(RequestStatusEnum.APPROVED)
                .productId("1")
                .requestId(requestId)
                .build();

        profileUpdateRequest = ProfileUpdateRequest.builder()
                .userId(userId)
                .requestId(requestId)
                .status(RequestStatusEnum.REJECTED)
                .build();

        profileValidationDetail = ProfileValidationDetail.builder()
                .requestId(requestId)
                .productId("1")
                .status(RequestStatusEnum.APPROVED)
                .build();
    }

    @Test
    public void monitorWithRejectedTest() {
        monitorDTO.setStatus(RequestStatusEnum.REJECTED);

        Mockito.when(profileUpdateRequestRepository.save(Mockito.any(ProfileUpdateRequest.class))).
                thenReturn(profileUpdateRequest);

        monitorService.monitorService(monitorDTO);

        Mockito.verify(profileUpdateRequestRepository, Mockito.times(1))
                .save(Mockito.any(ProfileUpdateRequest.class));
    }

    @Test
    public void monitorWithApprovedTest() {

        Mockito.when(profileValidationRepository.getProfileValidationsByRequestId(Mockito.anyString())).
                thenReturn(Arrays.asList(profileValidationDetail));

        Mockito.when(profileUpdateRequestRepository.save(Mockito.any(ProfileUpdateRequest.class))).
                thenReturn(profileUpdateRequest);

        Mockito.when(businessProfileRepository.save(Mockito.any(BusinessProfile.class))).
                thenReturn(businessProfile);

        monitorService.monitorService(monitorDTO);

        Mockito.verify(businessProfileRepository, Mockito.times(1))
                .save(Mockito.any(BusinessProfile.class));
    }
}
