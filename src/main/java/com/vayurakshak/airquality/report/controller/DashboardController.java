package com.vayurakshak.airquality.report.controller;

import com.vayurakshak.airquality.common.response.ApiResponse;
import com.vayurakshak.airquality.report.dto.DashboardResponse;
import com.vayurakshak.airquality.report.service.DashboardService;
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
