package com.vayurakshak.airquality.report.service;

import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.repository.AqiRepository;
import com.vayurakshak.airquality.alert.service.SmartAlertService;
import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.entity.SubscriptionPlan;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.report.dto.DashboardResponse;
import com.vayurakshak.airquality.report.entity.PollutionReport;
import com.vayurakshak.airquality.report.entity.ReportType;
import com.vayurakshak.airquality.report.repository.PollutionReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final Logger log =
            LoggerFactory.getLogger(DashboardService.class);

    private final OrganizationRepository organizationRepository;
    private final PollutionReportRepository reportRepository;
    private final AqiRepository aqiRepository;
    private final SmartAlertService smartAlertService;

    public DashboardResponse getDashboard(Long orgId) {

        log.info("Generating dashboard for orgId={}", orgId);

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> {
                    log.error("Dashboard failed: organization not found -> {}", orgId);
                    return new ResourceNotFoundException("Organization not found");
                });

        var reportsPage = reportRepository.findByOrganizationId(
                orgId,
                PageRequest.of(0, 100)
        );

        var reports = reportsPage.getContent();
        int totalReports = (int) reportsPage.getTotalElements();

        Map<ReportType, Long> reportsByType =
                reports.stream()
                        .collect(Collectors.groupingBy(
                                PollutionReport::getType,
                                Collectors.counting()
                        ));

        int aqiValue = aqiRepository
                .findTopByCityOrderByTimestampDesc(org.getCity())
                .map(AqiRecord::getAqi)
                .orElse(0);

        log.debug("AQI fetched for city={} value={}", org.getCity(), aqiValue);

        String aqiLevel = classifyAqi(aqiValue);
        int riskScore = calculateRiskScore(aqiValue, totalReports);

        String riskCategory = riskScore > 75 ? "High"
                : riskScore > 40 ? "Moderate"
                : "Low";

        String recommendation =
                riskCategory.equals("High")
                        ? "Avoid outdoor activities"
                        : "Normal precautions recommended";

        boolean advancedAnalytics =
                org.getPlan() == SubscriptionPlan.PRO ||
                        org.getPlan() == SubscriptionPlan.ENTERPRISE;

        String predictiveInsight = null;

        if (org.getPlan() == SubscriptionPlan.ENTERPRISE) {
            predictiveInsight = "Pollution expected to rise 12% tomorrow";
        }

        long hotspotCount = reportsByType.values()
                .stream()
                .filter(count -> count >= 3)
                .count();

        var alerts = smartAlertService.generateAlerts(
                aqiValue,
                riskScore,
                hotspotCount
        );

        return DashboardResponse.builder()
                .organizationName(org.getName())
                .city(org.getCity())
                .currentAqi(aqiValue)
                .aqiLevel(aqiLevel)
                .totalReports(totalReports)
                .reportsByType(
                        reportsByType.entrySet().stream()
                                .collect(Collectors.toMap(
                                        e -> e.getKey().name(),
                                        Map.Entry::getValue
                                ))
                )
                .riskScore(riskScore)
                .riskCategory(riskCategory)
                .recommendation(recommendation)
                .advancedAnalyticsEnabled(advancedAnalytics)
                .subscriptionPlan(org.getPlan().name())
                .predictiveInsight(predictiveInsight)
                .alerts(alerts)
                .build();
    }

    private String classifyAqi(int aqi) {
        if (aqi > 300) return "Severe";
        if (aqi > 200) return "Very Poor";
        if (aqi > 100) return "Moderate";
        return "Good";
    }

    private int calculateRiskScore(int aqi, int reports) {
        return Math.min(100, (aqi / 5) + (reports * 2));
    }
}
