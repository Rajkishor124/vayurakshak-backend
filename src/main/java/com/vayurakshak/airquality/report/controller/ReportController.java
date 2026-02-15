package com.vayurakshak.airquality.report.controller;

import com.vayurakshak.airquality.common.response.ApiResponse;
import com.vayurakshak.airquality.report.service.ReportService;
import com.vayurakshak.airquality.report.dto.PollutionReportRequest;
import com.vayurakshak.airquality.report.dto.ReportResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reports", description = "Pollution reporting & viewing")
@RestController
@RequestMapping("/api/v1/org/{orgId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Submit a pollution report")
    @PostMapping
    public ApiResponse<String> submitReport(
            @PathVariable Long orgId,
            @Valid @RequestBody PollutionReportRequest request) {

        request.setOrganizationId(orgId);
        reportService.submitReport(request);

        return ApiResponse.success("Report submitted successfully", null);
    }

    @Operation(summary = "Get pollution reports for an organization")
    @GetMapping
    public ApiResponse<Page<ReportResponse>> getReports(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.success(
                reportService.getReportsByOrganization(orgId, page, size));
    }
}
