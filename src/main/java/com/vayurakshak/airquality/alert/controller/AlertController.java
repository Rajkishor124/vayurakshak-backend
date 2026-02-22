package com.vayurakshak.airquality.alert.controller;

import com.vayurakshak.airquality.alert.dto.AlertResponse;
import com.vayurakshak.airquality.alert.service.AlertService;
import com.vayurakshak.airquality.shared.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/org/{orgId}/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public ApiResponse<Page<AlertResponse>> getAlerts(
            @PathVariable Long orgId,
            Pageable pageable) {

        return ApiResponse.success(
                alertService.getAlerts(orgId, pageable)
        );
    }

    @PutMapping("/{alertId}/acknowledge")
    public ApiResponse<String> acknowledgeAlert(
            @PathVariable Long orgId,
            @PathVariable Long alertId) {

        alertService.acknowledgeAlert(orgId, alertId);
        return ApiResponse.success("Alert acknowledged");
    }

    @DeleteMapping("/{alertId}")
    public ApiResponse<String> deleteAlert(
            @PathVariable Long orgId,
            @PathVariable Long alertId) {

        alertService.deleteAlert(orgId, alertId);
        return ApiResponse.success("Alert deleted successfully");
    }
}