package com.intuit.common.entity;

import com.intuit.profile.entity.BusinessProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileValidationDTO {

    private String userId;

    private String requestId;

    private BusinessProfile businessProfile;

    private List<Integer> productIds;
}
