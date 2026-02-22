package com.vayurakshak.airquality.advice.service;

import com.vayurakshak.airquality.advice.dto.AdviceRequest;
import com.vayurakshak.airquality.advice.dto.AdviceResponse;
import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.service.AqiService;
import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdviceService {

    private final AqiService aqiService;
    private final AiDecisionEngine aiDecisionEngine;

    public AdviceResponse getAdvice(AdviceRequest request) {

        String city = request.getCity().trim();

        log.info("Fetching AQI and generating advice for city: {}", city);

        AqiRecord record = aqiService.getLatestAqi(city);

        if (record == null) {
            throw new ResourceNotFoundException(
                    "No AQI data available for city: " + city
            );
        }

        return aiDecisionEngine.generateAdvice(
                record.getAqi(),
                request.getAge(),
                request.isHasRespiratoryIssue()
        );
    }
}