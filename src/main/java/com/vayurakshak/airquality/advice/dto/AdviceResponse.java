package com.vayurakshak.airquality.advice.dto;

import com.vayurakshak.airquality.advice.enums.RiskLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdviceResponse {

    private RiskLevel riskLevel;

    private List<String> recommendations;
}