package com.vayurakshak.airquality.aqi.controller;

import com.vayurakshak.airquality.aqi.dto.AqiResponse;
import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.enums.AqiCategory;
import com.vayurakshak.airquality.aqi.service.AqiService;
import com.vayurakshak.airquality.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/aqi")
@RequiredArgsConstructor
@Validated
public class AqiController {

    private final AqiService service;

    @Operation(summary = "Get latest AQI for a city")
    @GetMapping("/latest")
    public ApiResponse<AqiResponse> getLatestAqi(
            @RequestParam @NotBlank String city) {

        AqiRecord record = service.getLatestAqi(city);

        AqiCategory category =
                service.classifyAqi(record.getAqi());

        AqiResponse response = AqiResponse.builder()
                .city(record.getCity())
                .aqi(record.getAqi())
                .level(category.name().replace("_", " "))
                .summary(service.buildSummary(category))
                .build();

        return ApiResponse.success(response);
    }
}