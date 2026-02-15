package com.vayurakshak.airquality.alert.service;

import com.vayurakshak.airquality.alert.dto.AlertResponse;
import com.vayurakshak.airquality.alert.entity.AlertSeverity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SmartAlertService {

    public List<AlertResponse> generateAlerts(int aqiValue,
                                              int riskScore,
                                              long hotspotCount) {

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
