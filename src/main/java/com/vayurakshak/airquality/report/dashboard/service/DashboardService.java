package com.vayurakshak.airquality.report.dashboard.service;

import com.vayurakshak.airquality.aqi.service.AqiService;
import com.vayurakshak.airquality.alert.service.SmartAlertService;
import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.infrastructure.security.OrgValidationUtil;
import com.vayurakshak.airquality.infrastructure.security.feature.Feature;
import com.vayurakshak.airquality.infrastructure.security.feature.FeatureGateService;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.report.dashboard.dto.DashboardResponse;
import com.vayurakshak.airquality.report.core.repository.PollutionReportRepository;
import com.vayurakshak.airquality.report.enums.ReportType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrgValidationUtil orgValidationUtil;
    private final FeatureGateService featureGateService;
    private final OrganizationRepository organizationRepository;
    private final PollutionReportRepository reportRepository;
    private final AqiService aqiService;
    private final SmartAlertService smartAlertService;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long orgId) {

        // 🔐 Multi-tenant isolation
        orgValidationUtil.validateOrgAccess(orgId);

        // 🎛 Subscription gating
        featureGateService.checkAccess(Feature.DASHBOARD_ANALYTICS);

        log.debug("Generating dashboard for orgId={}", orgId);

        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Organization not found"));

        // ✅ Optimized total count
        long totalReports =
                reportRepository.countByOrganizationIdAndDeletedFalse(orgId);

        // ✅ Optimized grouped count by type (single DB query)
        Map<String, Long> reportsByType =
                reportRepository.countReportsByType(orgId)
                        .stream()
                        .collect(Collectors.toMap(
                                obj -> ((ReportType) obj[0]).name(),
                                obj -> (Long) obj[1]
                        ));

        // Ensure all enum types appear (even if 0)
        for (ReportType type : ReportType.values()) {
            reportsByType.putIfAbsent(type.name(), 0L);
        }

        int aqiValue = aqiService
                .getLatestAqi(org.getCity())
                .getAqi();

        int riskScore = calculateRiskScore(aqiValue, (int) totalReports);
        String riskCategory = calculateRiskCategory(riskScore);

        long hotspotCount =
                reportRepository.countReportsByLocation(orgId)
                        .stream()
                        .filter(obj -> (Long) obj[1] >= 3)
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
                .aqiLevel(aqiService.classifyAqi(aqiValue).name())
                .totalReports((int) totalReports)
                .reportsByType(reportsByType)
                .riskScore(riskScore)
                .riskCategory(riskCategory)
                .recommendation(
                        riskScore > 75
                                ? "Avoid outdoor activities"
                                : "Normal precautions recommended"
                )
                .subscriptionPlan(org.getPlan().name())
                .alerts(alerts)
                .build();
    }

    private int calculateRiskScore(int aqi, int reports) {
        return Math.min(100, (aqi / 5) + (reports * 2));
    }

    private String calculateRiskCategory(int riskScore) {
        if (riskScore > 75) return "High";
        if (riskScore > 40) return "Moderate";
        return "Low";
    }
}