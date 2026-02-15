package com.vayurakshak.airquality.aqi.service;

import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.repository.AqiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AqiService {

    private final AqiRepository repository;

    public AqiRecord getLatestAqi(String city) {
        return repository.findTopByCityOrderByTimestampDesc(city)
                .orElseThrow(() -> new RuntimeException("AQI data not found"));
    }

    public String classifyAqi(int aqi) {
        if (aqi > 300) return "Severe";
        if (aqi > 200) return "Very Poor";
        if (aqi > 100) return "Moderate";
        return "Good";
    }
}
