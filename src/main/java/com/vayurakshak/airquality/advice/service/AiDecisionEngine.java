package com.vayurakshak.airquality.advice.service;

import com.vayurakshak.airquality.advice.dto.AdviceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiDecisionEngine {

    public AdviceResponse generateAdvice(int aqi, int age, boolean hasIssue) {

        if (aqi > 300) {
            return AdviceResponse.builder()
                    .riskLevel("High")
                    .recommendations(List.of(
                            "Avoid outdoor activities",
                            "Wear N95 mask if going outside",
                            "Keep windows closed",
                            "Use air purifier if available"
                    ))
                    .build();
        }

        if (aqi > 200 && (age > 60 || hasIssue)) {
            return AdviceResponse.builder()
                    .riskLevel("Moderate")
                    .recommendations(List.of(
                            "Limit outdoor exposure",
                            "Avoid heavy exercise",
                            "Prefer indoor workouts"
                    ))
                    .build();
        }

        return AdviceResponse.builder()
                .riskLevel("Low")
                .recommendations(List.of(
                        "Outdoor activities are safe",
                        "Maintain regular precautions"
                ))
                .build();
    }
}
