package com.vayurakshak.airquality.advice.service;

import com.vayurakshak.airquality.advice.dto.AdviceResponse;
import com.vayurakshak.airquality.advice.enums.RiskLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AiDecisionEngine {

    public AdviceResponse generateAdvice(int aqi, int age, boolean hasIssue) {

        log.info("Generating advice for AQI: {}, Age: {}, RespiratoryIssue: {}",
                aqi, age, hasIssue);

        if (aqi > 300) {
            return buildResponse(RiskLevel.HIGH, List.of(
                    "Avoid outdoor activities",
                    "Wear N95 mask if going outside",
                    "Keep windows closed",
                    "Use air purifier if available"
            ));
        }

        if (aqi > 200 && (age > 60 || hasIssue)) {
            return buildResponse(RiskLevel.MODERATE, List.of(
                    "Limit outdoor exposure",
                    "Avoid heavy exercise",
                    "Prefer indoor workouts"
            ));
        }

        return buildResponse(RiskLevel.LOW, List.of(
                "Outdoor activities are safe",
                "Maintain regular precautions"
        ));
    }

    private AdviceResponse buildResponse(RiskLevel level, List<String> recommendations) {
        return AdviceResponse.builder()
                .riskLevel(level)
                .recommendations(recommendations)
                .build();
    }
}