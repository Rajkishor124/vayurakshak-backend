package com.vayurakshak.airquality.report.submission.controller;

import com.vayurakshak.airquality.report.submission.service.ReportService;
import com.vayurakshak.airquality.shared.response.ApiResponse;
import com.vayurakshak.airquality.report.submission.dto.PollutionReportRequest;
import com.vayurakshak.airquality.report.submission.dto.ReportResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Reports", description = "Pollution reporting & viewing")
@RestController
@RequestMapping("/api/v1/org/{orgId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ApiResponse<String> submitReport(
            @PathVariable Long orgId,
            @Valid @RequestBody PollutionReportRequest request) {

        reportService.submitReport(orgId, request);

        return ApiResponse.success("Report submitted successfully", null);
    }

    @GetMapping
    public ApiResponse<Page<ReportResponse>> getReports(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        return ApiResponse.success(
                reportService.getReportsByOrganization(orgId, pageable));
    }

    @DeleteMapping("/{reportId}")
    public ApiResponse<String> deleteReport(
            @PathVariable Long orgId,
            @PathVariable Long reportId) {

        reportService.deleteReport(orgId, reportId);

        return ApiResponse.success("Report deleted successfully", null);
    }
}