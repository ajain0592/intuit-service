package com.intuit.quickbook.service.impl;

import com.intuit.common.config.kafka.producer.KafkaProducerService;
import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.common.entity.MonitorDTO;
import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.common.entity.ProfileValidationDetail;
import com.intuit.common.repository.ProfileValidationRepository;
import com.intuit.profile.entity.BusinessAddress;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.entity.TaxIdentifiers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class QBValidateProfileImplTest {

    MonitorDTO monitorDTO;

    BusinessProfile businessProfile;

    @InjectMocks
    QBValidateProfileImpl qbValidateProfile;

    @Mock
    private ProfileValidationRepository profileValidationRepository;

    @Mock
    KafkaProducerService kafkaProducerService;

    private ProfileValidationDetail profileValidationDetail;

    private ProfileValidationDTO profileValidationDTO;

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

        profileValidationDetail = ProfileValidationDetail.builder()
                .requestId(requestId)
                .productId("1")
                .status(RequestStatusEnum.APPROVED)
                .build();

        profileValidationDTO = ProfileValidationDTO.builder()
                .requestId(requestId)
                .userId(userId)
                .productIds(Arrays.asList(1,2))
                .businessProfile(businessProfile)
                .build();
    }

    @Test
    public void processWithSuccessfulValidationTest() {

        Mockito.when(profileValidationRepository.save(Mockito.any(ProfileValidationDetail.class))).
                thenReturn(profileValidationDetail);

        Mockito.doNothing().when(kafkaProducerService).sendMonitorEvent(Mockito.any(MonitorDTO.class));

        qbValidateProfile.process(profileValidationDTO);

        Mockito.verify(profileValidationRepository, Mockito.times(2))
                .save(Mockito.any(ProfileValidationDetail.class));

        Mockito.verify(kafkaProducerService, Mockito.times(1))
                .sendMonitorEvent(Mockito.any(MonitorDTO.class));
    }
}
