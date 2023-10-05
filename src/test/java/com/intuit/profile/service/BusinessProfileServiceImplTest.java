package com.intuit.profile.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.intuit.common.config.kafka.producer.KafkaProducerService;
import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.common.entity.ProfileUpdateRequest;
import com.intuit.common.entity.ProfileValidationDetail;
import com.intuit.common.entity.UserProductMapping;
import com.intuit.common.repository.ProfileUpdateRequestRepository;
import com.intuit.common.repository.ProfileValidationRepository;
import com.intuit.common.repository.UserProductMappingRepository;
import com.intuit.profile.entity.BusinessAddress;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.entity.TaxIdentifiers;
import com.intuit.profile.repository.BusinessProfileRepository;
import com.intuit.profile.service.impl.BusinessProfileServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class BusinessProfileServiceImplTest {

    @InjectMocks
    BusinessProfileServiceImpl businessProfileService;

    @Mock
    private BusinessProfileRepository businessProfileRepository;

    @Mock
    private ProfileUpdateRequestRepository profileUpdateRequestRepository;

    @Mock
    private ProfileValidationRepository profileValidationRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private UserProductMappingRepository userProductMappingRepository;

    @Mock
    private ProfileUpdateRequest profileUpdateRequestMock;

    @Mock
    private ProfileValidationDetail profileValidationDetailMock;

    private BusinessProfile businessProfile;

    private ProfileUpdateRequest profileUpdateRequest;

    private UserProductMapping userProductMapping;

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

        userProductMapping = UserProductMapping.builder()
                .userId("1")
                .productId("1")
                .build();
    }

    @Test
    public void acceptRequestTest() {
        String userId = "1";

        String requestId = businessProfileService.
                acceptRequest(userId, businessProfile);

        Assertions.assertNotNull(requestId);
    }

    @Test
    public void getBusinessProfileTest()  {
        String userId = "1";
        String companyName = "Test";

        Mockito.when(businessProfileRepository.
                getBusinessProfileByUserIdAndCompanyName(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(businessProfile);

        BusinessProfile actualResult = businessProfileService.
                getBusinessProfile(userId, companyName);

        Assertions.assertEquals(actualResult, businessProfile);
    }

    @Test
    public void getStatusTest()  {
        String userId = "1";
        String companyName = "";

        Mockito.when(profileUpdateRequestRepository.
                        getProfileUpdateStatus(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(profileUpdateRequest);

        ProfileUpdateRequest actualResult = businessProfileService.
                getStatus(userId, companyName);

        Assertions.assertEquals(actualResult, profileUpdateRequest);
    }

    @Test
    public void updateAndProduceTest() throws JsonProcessingException {

        Mockito.when(profileUpdateRequestRepository.
                        save(Mockito.any(ProfileUpdateRequest.class)))
                .thenReturn(profileUpdateRequestMock);

        Mockito.when(profileValidationRepository.
                        save(Mockito.any(ProfileValidationDetail.class)))
                .thenReturn(profileValidationDetailMock);

        Mockito.when(userProductMappingRepository.
                        getUserProductMapping(Mockito.anyString()))
                .thenReturn(Arrays.asList(userProductMapping));

        Mockito.doNothing().when(kafkaProducerService)
                .sendProfileUpdateEvent(Mockito.any(ProfileValidationDTO.class));

        businessProfileService.updateAndProduce(userId, requestId, businessProfile);

        Mockito.verify(profileUpdateRequestRepository, Mockito.times(2))
                .save(Mockito.any(ProfileUpdateRequest.class));

        Mockito.verify(profileValidationRepository, Mockito.times(1))
                .save(Mockito.any(ProfileValidationDetail.class));

        Mockito.verify(kafkaProducerService, Mockito.times(1))
                .sendProfileUpdateEvent(Mockito.any(ProfileValidationDTO.class));
    }


}
