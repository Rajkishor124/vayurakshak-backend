package com.vayurakshak.airquality.advice.service;

import com.vayurakshak.airquality.advice.dto.AdviceRequest;
import com.vayurakshak.airquality.advice.dto.AdviceResponse;
import com.vayurakshak.airquality.aqi.service.AqiService;
import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdviceService {

    private final AqiService aqiService;
    private final AiDecisionEngine aiDecisionEngine;

    public AdviceResponse getAdvice(AdviceRequest request) {
        AqiRecord record = aqiService.getLatestAqi(request.getCity());
        return aiDecisionEngine.generateAdvice(
                record.getAqi(),
                request.getAge(),
                request.isHasRespiratoryIssue()
        );
    }
}
