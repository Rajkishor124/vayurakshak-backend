package com.vayurakshak.airquality.report.controller;

import com.vayurakshak.airquality.infrastructure.common.response.ApiResponse;
import com.vayurakshak.airquality.report.dto.AdminSummary;
import com.vayurakshak.airquality.report.dto.ReportResponse;
import com.vayurakshak.airquality.report.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Administrative monitoring & analytics")
@RestController
@RequestMapping("/api/v1/org/{orgId}/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ReportService reportService;

    // ✅ Paginated reports (admin view)
    @GetMapping("/reports")
    public ApiResponse<Page<ReportResponse>> getReports(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.success(
                reportService.getReportsByOrganization(orgId, page, size));
    }

    @GetMapping("/summary")
    public ApiResponse<AdminSummary> summary(@PathVariable Long orgId) {

        long totalReports = reportService
                .getReportsByOrganization(orgId, 0, 1)
                .getTotalElements();

        String insight = totalReports > 10
                ? "High pollution activity detected"
                : "Pollution levels under control";

        return ApiResponse.success(
                new AdminSummary(totalReports, insight));
    }
}
