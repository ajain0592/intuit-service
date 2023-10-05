package com.intuit.quickbook.consumer;

import com.intuit.common.entity.ProfileValidationDTO;
import com.intuit.profile.entity.BusinessAddress;
import com.intuit.profile.entity.BusinessProfile;
import com.intuit.profile.entity.TaxIdentifiers;
import com.intuit.quickbook.service.QBValidateProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class QuickBookConsumerTest {

    BusinessProfile businessProfile;

    @InjectMocks
    QuickbookConsumer quickbookConsumer;

    @Mock
    QBValidateProfile qbValidateProfile;

    private final String userId = "1";

    private final String requestId = "a5b70d98-fcd3-454c-8200-c4d90d225e81";

    ProfileValidationDTO profileValidationDTO;

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

        profileValidationDTO = ProfileValidationDTO.builder()
                .userId(userId)
                .businessProfile(businessProfile)
                .productIds(Arrays.asList(1,2))
                .requestId(requestId)
                .build();
    }

    @Test
    public void listenTest() {
        Mockito.doNothing().when(qbValidateProfile)
                .process(Mockito.any(ProfileValidationDTO.class));

        ReflectionTestUtils.setField(quickbookConsumer, "productId", 1);

        quickbookConsumer.listen(profileValidationDTO);

       Mockito.verify(qbValidateProfile, Mockito.times(1))
               .process(Mockito.any(ProfileValidationDTO.class));
    }
}
