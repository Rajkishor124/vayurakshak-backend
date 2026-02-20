package com.vayurakshak.airquality.advice.controller;

import com.vayurakshak.airquality.advice.dto.AdviceRequest;
import com.vayurakshak.airquality.advice.dto.AdviceResponse;
import com.vayurakshak.airquality.advice.service.AdviceService;
import com.vayurakshak.airquality.infrastructure.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/advice")
@RequiredArgsConstructor
public class AdviceController {

    private final AdviceService adviceService;

    @PostMapping
    public ApiResponse<AdviceResponse> getPersonalizedAdvice(
            @Valid @RequestBody AdviceRequest request) {

        return ApiResponse.success(
                adviceService.getAdvice(request)
        );
    }
}
