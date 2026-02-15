package com.vayurakshak.airquality.aqi.controller;

import com.vayurakshak.airquality.aqi.dto.AqiResponse;
import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.service.AqiService;
import com.vayurakshak.airquality.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/aqi")
@RequiredArgsConstructor
public class AqiController {

    private final AqiService service;

    @Operation(summary = "Get today's AQI for a city")
    @GetMapping("/today")
    public ApiResponse<AqiResponse> getTodayAqi(
            @RequestParam @NotBlank String city) {

        AqiRecord record = service.getLatestAqi(city);
        String level = service.classifyAqi(record.getAqi());

        AqiResponse response = AqiResponse.builder()
                .city(record.getCity())
                .aqi(record.getAqi())
                .level(level)
                .summary("Air quality is " + level + " today")
                .build();

        return ApiResponse.success(response);
    }
}
