package com.vayurakshak.airquality.report.admin.service;

import com.vayurakshak.airquality.infrastructure.exception.UnauthorizedException;
import com.vayurakshak.airquality.infrastructure.security.OrgValidationUtil;
import com.vayurakshak.airquality.infrastructure.security.context.CurrentUserProvider;
import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import com.vayurakshak.airquality.report.admin.dto.AdminSummary;
import com.vayurakshak.airquality.report.submission.dto.ReportResponse;
import com.vayurakshak.airquality.report.core.repository.PollutionReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final PollutionReportRepository repository;
    private final OrgValidationUtil orgValidationUtil;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public Page<ReportResponse> getReports(Long orgId, int page, int size) {

        validateAdmin(orgId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return repository
                .findByOrganizationIdAndDeletedFalse(orgId, pageable)
                .map(report -> ReportResponse.builder()
                        .id(report.getId())
                        .type(report.getType().name())
                        .location(report.getLocation())
                        .description(report.getDescription())
                        .createdAt(report.getCreatedAt())
                        .build());
    }

    @Transactional(readOnly = true)
    public AdminSummary getSummary(Long orgId) {

        validateAdmin(orgId);

        long totalReports = repository.countByOrganizationIdAndDeletedFalse(orgId);

        String insight = totalReports > 10
                ? "High pollution activity detected"
                : "Pollution levels under control";

        return new AdminSummary(totalReports, insight);
    }

    private void validateAdmin(Long orgId) {

        orgValidationUtil.validateOrgAccess(orgId);

        if (!"ROLE_ADMIN".equals(currentUserProvider.getCurrentRole())) {
            throw new UnauthorizedException("Admin access required");
        }
    }
}