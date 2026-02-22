package com.vayurakshak.airquality.aqi.service;

import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.enums.AqiCategory;
import com.vayurakshak.airquality.aqi.repository.AqiRepository;
import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AqiService {

    private final AqiRepository repository;

    @Transactional(readOnly = true)
    public AqiRecord getLatestAqi(String city) {

        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City must not be empty");
        }

        String normalizedCity = city.trim();

        log.debug("Fetching latest AQI for city={}", normalizedCity);

        return repository
                .findTopByCityIgnoreCaseAndDeletedFalseOrderByTimestampDesc(normalizedCity)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "AQI data not found for city: " + normalizedCity
                        ));
    }

    public AqiCategory classifyAqi(int aqi) {

        if (aqi > 300) return AqiCategory.SEVERE;
        if (aqi > 200) return AqiCategory.VERY_POOR;
        if (aqi > 100) return AqiCategory.MODERATE;
        return AqiCategory.GOOD;
    }

    public String buildSummary(AqiCategory category) {
        return "Air quality is " + category.name().replace("_", " ") + " today";
    }
}