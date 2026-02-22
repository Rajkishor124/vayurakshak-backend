package com.vayurakshak.airquality.alert.service;

import com.vayurakshak.airquality.alert.dto.AlertResponse;
import com.vayurakshak.airquality.alert.enums.AlertSeverity;
import com.vayurakshak.airquality.infrastructure.security.feature.Feature;
import com.vayurakshak.airquality.infrastructure.security.feature.FeatureGateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmartAlertService {

    private final FeatureGateService featureGateService;

    public List<AlertResponse> generateAlerts(int aqiValue,
                                              int riskScore,
                                              long hotspotCount) {

        // 🔐 Subscription-based gating
        featureGateService.checkAccess(Feature.SMART_ALERTS);

        validateInputs(aqiValue, riskScore, hotspotCount);

        log.debug("Generating smart alerts: AQI={}, RiskScore={}, Hotspots={}",
                aqiValue, riskScore, hotspotCount);

        List<AlertResponse> alerts = new ArrayList<>();

        evaluateAqiAlerts(alerts, aqiValue);
        evaluateRiskAlerts(alerts, riskScore);
        evaluateHotspotAlerts(alerts, hotspotCount);

        if (alerts.isEmpty()) {
            alerts.add(buildAlert(
                    "Air Quality Normal",
                    "Air quality is under control.",
                    AlertSeverity.INFO
            ));
        }

        return alerts;
    }

    private void validateInputs(int aqiValue, int riskScore, long hotspotCount) {

        if (aqiValue < 0 || riskScore < 0 || hotspotCount < 0) {
            throw new IllegalArgumentException("Alert metrics cannot be negative");
        }
    }

    private void evaluateAqiAlerts(List<AlertResponse> alerts, int aqiValue) {

        if (aqiValue > 300) {
            alerts.add(buildAlert(
                    "Severe Air Quality",
                    "AQI is in Severe range. Avoid outdoor activity.",
                    AlertSeverity.CRITICAL
            ));
        } else if (aqiValue > 200) {
            alerts.add(buildAlert(
                    "Unhealthy Air Quality",
                    "Air quality is very poor. Limit prolonged outdoor exposure.",
                    AlertSeverity.HIGH
            ));
        }
    }

    private void evaluateRiskAlerts(List<AlertResponse> alerts, int riskScore) {

        if (riskScore > 75) {
            alerts.add(buildAlert(
                    "High Health Risk",
                    "Organization risk score is high. Sensitive groups should stay indoors.",
                    AlertSeverity.HIGH
            ));
        }
    }

    private void evaluateHotspotAlerts(List<AlertResponse> alerts, long hotspotCount) {

        if (hotspotCount > 0) {
            alerts.add(buildAlert(
                    "Pollution Hotspots Detected",
                    "Multiple pollution complaints detected. Authorities may need to investigate.",
                    AlertSeverity.MEDIUM
            ));
        }
    }

    private AlertResponse buildAlert(String title,
                                     String message,
                                     AlertSeverity severity) {

        return AlertResponse.builder()
                .title(title)
                .message(message)
                .severity(severity)
                .build();
    }
}