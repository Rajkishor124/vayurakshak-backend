package com.vayurakshak.airquality.report.service;

import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.report.dto.PollutionReportRequest;
import com.vayurakshak.airquality.report.dto.ReportResponse;
import com.vayurakshak.airquality.report.entity.PollutionReport;
import com.vayurakshak.airquality.report.repository.PollutionReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private static final Logger log =
            LoggerFactory.getLogger(ReportService.class);

    private final PollutionReportRepository pollutionReportRepository;
    private final OrganizationRepository organizationRepository;

    public void submitReport(PollutionReportRequest request) {

        log.info("Submitting pollution report for orgId={}, type={}, location={}",
                request.getOrganizationId(),
                request.getType(),
                request.getLocation());

        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> {
                    log.error("Organization not found: {}", request.getOrganizationId());
                    return new ResourceNotFoundException("Organization not found");
                });

        PollutionReport report = PollutionReport.builder()
                .type(request.getType())   // enum
                .location(request.getLocation())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .organization(organization)
                .build();

        pollutionReportRepository.save(report);

        log.info("Report saved successfully for orgId={}", organization.getId());
    }

    public Page<ReportResponse> getReportsByOrganization(
            Long organizationId,
            int page,
            int size) {

        log.debug("Fetching reports for orgId={}, page={}, size={}",
                organizationId, page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<ReportResponse> result = pollutionReportRepository
                .findByOrganizationId(organizationId, pageable)
                .map(report -> ReportResponse.builder()
                        .id(report.getId())
                        .type(report.getType().name()) // return string for frontend clarity
                        .location(report.getLocation())
                        .description(report.getDescription())
                        .createdAt(report.getCreatedAt())
                        .build());

        log.debug("Fetched {} reports for orgId={}",
                result.getTotalElements(),
                organizationId);

        return result;
    }
}
