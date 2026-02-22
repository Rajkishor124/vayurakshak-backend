package com.vayurakshak.airquality.report.submission.service;

import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.infrastructure.exception.UnauthorizedException;
import com.vayurakshak.airquality.infrastructure.security.OrgValidationUtil;
import com.vayurakshak.airquality.infrastructure.security.context.CurrentUserProvider;
import com.vayurakshak.airquality.infrastructure.security.feature.Feature;
import com.vayurakshak.airquality.infrastructure.security.feature.FeatureGateService;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.report.submission.dto.PollutionReportRequest;
import com.vayurakshak.airquality.report.submission.dto.ReportResponse;
import com.vayurakshak.airquality.report.core.entity.PollutionReport;
import com.vayurakshak.airquality.report.core.repository.PollutionReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final PollutionReportRepository reportRepository;
    private final OrganizationRepository organizationRepository;
    private final OrgValidationUtil orgValidationUtil;
    private final FeatureGateService featureGateService;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    public void submitReport(Long orgId, PollutionReportRequest request) {

        // 🔐 Strict org isolation
        orgValidationUtil.validateOrgAccess(orgId);

        featureGateService.checkAccess(Feature.REPORT_SUBMISSION);

        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Organization not found"));

        enforcePlanSubmissionLimit(organization);

        PollutionReport report = PollutionReport.builder()
                .type(request.getType())
                .location(request.getLocation())
                .description(request.getDescription())
                .organization(organization)
                .build();

        reportRepository.save(report);

        log.info("Report submitted: orgId={}, type={}", orgId, request.getType());
    }

    @Transactional(readOnly = true)
    public Page<ReportResponse> getReportsByOrganization(
            Long orgId,
            Pageable pageable) {

        orgValidationUtil.validateOrgAccess(orgId);
        featureGateService.checkAccess(Feature.REPORT_VIEW);

        return reportRepository
                .findByOrganizationIdAndDeletedFalse(orgId, pageable)
                .map(report -> ReportResponse.builder()
                        .id(report.getId())
                        .type(report.getType().name())
                        .location(report.getLocation())
                        .description(report.getDescription())
                        .createdAt(report.getCreatedAt())
                        .build());
    }

    @Transactional
    public void deleteReport(Long orgId, Long reportId) {

        orgValidationUtil.validateOrgAccess(orgId);

        if (!"ROLE_ADMIN".equals(currentUserProvider.getCurrentRole())) {
            throw new UnauthorizedException("Only admin can delete reports");
        }

        PollutionReport report = reportRepository
                .findByIdAndOrganizationIdAndDeletedFalse(reportId, orgId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found"));

        report.setDeleted(true);

        log.info("Report soft deleted: {}", reportId);
    }

    // 🏆 Plan-based submission limits
    private void enforcePlanSubmissionLimit(Organization org) {

        long totalReports =
                reportRepository.countByOrganizationIdAndDeletedFalse(org.getId());

        if (org.getPlan() == SubscriptionPlan.FREE && totalReports >= 50) {
            throw new UnauthorizedException(
                    "Free plan limit reached (50 reports). Upgrade required.");
        }

        if (org.getPlan() == SubscriptionPlan.PRO && totalReports >= 500) {
            throw new UnauthorizedException(
                    "Pro plan limit reached (500 reports). Upgrade required.");
        }
    }
}