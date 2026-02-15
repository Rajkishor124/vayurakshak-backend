package com.vayurakshak.airquality.advice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdviceResponse {
    private String riskLevel;
    private List<String> recommendations;
}
