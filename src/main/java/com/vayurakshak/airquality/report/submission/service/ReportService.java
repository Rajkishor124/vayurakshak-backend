package com.vayurakshak.airquality.report.submission.service;

import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.infrastructure.security.OrgValidationUtil;
import com.vayurakshak.airquality.infrastructure.security.context.CurrentUserProvider;
import com.vayurakshak.airquality.infrastructure.security.feature.Feature;
import com.vayurakshak.airquality.infrastructure.security.feature.FeatureGateService;
import com.vayurakshak.airquality.organization.entity.Organization;
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

    /**
     * Submit new pollution report
     */
    @Transactional
    public void submitReport(PollutionReportRequest request) {

        Long orgId = currentUserProvider.getCurrentOrganizationId();

        orgValidationUtil.validateOrgAccess(orgId);

        // 🎛 Feature gating (basic reports allowed for FREE)
        featureGateService.checkAccess(Feature.BASIC_ALERTS);

        log.info("Submitting report for orgId={}, type={}, location={}",
                orgId,
                request.getType(),
                request.getLocation());

        Organization organization = organizationRepository.findById(orgId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Organization not found"));

        PollutionReport report = PollutionReport.builder()
                .type(request.getType())
                .location(request.getLocation())
                .description(request.getDescription())
                .organization(organization)
                .build();

        reportRepository.save(report);

        log.info("Report saved successfully for orgId={}", orgId);
    }

    /**
     * Fetch reports (multi-tenant safe + soft delete safe)
     */
    @Transactional(readOnly = true)
    public Page<ReportResponse> getReportsByOrganization(
            Long orgId,
            Pageable pageable) {

        orgValidationUtil.validateOrgAccess(orgId);

        featureGateService.checkAccess(Feature.BASIC_ALERTS);

        log.debug("Fetching reports for orgId={}", orgId);

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

    /**
     * Soft delete report
     */
    @Transactional
    public void deleteReport(Long orgId, Long reportId) {

        orgValidationUtil.validateOrgAccess(orgId);

        PollutionReport report = reportRepository
                .findByIdAndOrganizationIdAndDeletedFalse(reportId, orgId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Report not found"));

        report.setDeleted(true);

        log.info("Report soft deleted: {}", reportId);
    }
}