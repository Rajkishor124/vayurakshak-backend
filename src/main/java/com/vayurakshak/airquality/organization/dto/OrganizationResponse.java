package com.vayurakshak.airquality.organization.dto;

import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrganizationResponse {

    private Long id;
    private String name;
    private String type;
    private String city;
    private SubscriptionPlan plan;
}