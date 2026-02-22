package com.vayurakshak.airquality.report.admin.controller;

import com.vayurakshak.airquality.shared.response.ApiResponse;
import com.vayurakshak.airquality.report.admin.dto.AdminSummary;
import com.vayurakshak.airquality.report.admin.service.AdminReportService;
import com.vayurakshak.airquality.report.submission.dto.ReportResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin", description = "Administrative monitoring & analytics")
@RestController
@RequestMapping("/api/v1/org/{orgId}/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminReportService adminService;

    @GetMapping("/reports")
    public ApiResponse<Page<ReportResponse>> getReports(
            @PathVariable Long orgId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ApiResponse.success(
                adminService.getReports(orgId, page, size)
        );
    }

    @GetMapping("/summary")
    public ApiResponse<AdminSummary> summary(@PathVariable Long orgId) {

        return ApiResponse.success(
                adminService.getSummary(orgId)
        );
    }
}