package com.vayurakshak.airquality.report.controller;

import com.vayurakshak.airquality.common.response.ApiResponse;
import com.vayurakshak.airquality.report.dto.HotspotResponse;
import com.vayurakshak.airquality.report.service.HotspotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Hotspots", description = "Pollution hotspot detection")
@RestController
@RequestMapping("/api/v1/org/{orgId}/hotspots")
@RequiredArgsConstructor
public class HotspotController {

    private final HotspotService hotspotService;

    @GetMapping
    public ApiResponse<List<HotspotResponse>> getHotspots(@PathVariable Long orgId) {
        return ApiResponse.success(hotspotService.detectHotspots(orgId));
    }
}
