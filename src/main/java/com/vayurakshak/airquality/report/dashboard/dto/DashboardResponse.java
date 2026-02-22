package com.vayurakshak.airquality.report.dashboard.dto;

import com.vayurakshak.airquality.alert.dto.AlertResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DashboardResponse {

    private String organizationName;
    private String city;

    private int currentAqi;
    private String aqiLevel;

    private int totalReports;
    private Map<String, Long> reportsByType;

    private int riskScore;
    private String riskCategory;

    private String recommendation;

    private boolean advancedAnalyticsEnabled;
    private String subscriptionPlan;
    private String predictiveInsight;

    private List<AlertResponse> alerts;
}
