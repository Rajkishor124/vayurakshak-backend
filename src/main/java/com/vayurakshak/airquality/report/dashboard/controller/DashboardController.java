package com.vayurakshak.airquality.report.dashboard.controller;

import com.vayurakshak.airquality.shared.response.ApiResponse;
import com.vayurakshak.airquality.report.dashboard.dto.DashboardResponse;
import com.vayurakshak.airquality.report.dashboard.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dashboard", description = "AQI insights & risk analytics")
@RestController
@RequestMapping("/api/v1/org/{orgId}/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ApiResponse<DashboardResponse> getDashboard(
            @PathVariable Long orgId) {

        return ApiResponse.success(
                dashboardService.getDashboard(orgId)
        );
    }
}
