package com.intuit.common.entity;

import com.intuit.common.constant.RequestStatusEnum;
import com.intuit.profile.entity.BusinessProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitorDTO {

    String userId;

    String requestId;

    String productId;

    RequestStatusEnum status;

    private BusinessProfile businessProfile;

}
