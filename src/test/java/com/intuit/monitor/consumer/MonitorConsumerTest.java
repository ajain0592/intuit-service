package com.intuit.monitor.consumer;

import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.common.entity.MonitorDTO;
import com.intuit.monitor.service.MonitorService;
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

@ExtendWith(MockitoExtension.class)
public class MonitorConsumerTest {

    MonitorDTO monitorDTO;

    BusinessProfile businessProfile;

    @InjectMocks
    MonitorConsumer monitorConsumer;

    @Mock
    MonitorService monitorService;

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
    }

    @Test
    public void listenTest() {
        Mockito.doNothing().when(monitorService)
                .monitorService(Mockito.any(MonitorDTO.class));

        monitorConsumer.listen(monitorDTO);

       Mockito.verify(monitorService, Mockito.times(1))
               .monitorService(Mockito.any(MonitorDTO.class));
    }
}
