package com.vayurakshak.airquality.alert.service;

import com.vayurakshak.airquality.alert.dto.AlertResponse;
import com.vayurakshak.airquality.alert.entity.Alert;
import com.vayurakshak.airquality.alert.repository.AlertRepository;
import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.infrastructure.security.OrgValidationUtil;
import com.vayurakshak.airquality.infrastructure.security.feature.Feature;
import com.vayurakshak.airquality.infrastructure.security.feature.FeatureGateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final OrgValidationUtil orgValidationUtil;
    private final FeatureGateService featureGateService;

    @Transactional(readOnly = true)
    public Page<AlertResponse> getAlerts(Long orgId, Pageable pageable) {

        // 1️⃣ Multi-tenant validation
        orgValidationUtil.validateOrgAccess(orgId);

        // 2️⃣ Subscription feature gating
        featureGateService.checkAccess(Feature.BASIC_ALERTS);

        log.debug("Fetching alerts for orgId: {}", orgId);

        return alertRepository
                .findByOrganizationIdAndDeletedFalse(orgId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public void acknowledgeAlert(Long orgId, Long alertId) {

        orgValidationUtil.validateOrgAccess(orgId);
        featureGateService.checkAccess(Feature.BASIC_ALERTS);

        Alert alert = alertRepository
                .findByIdAndOrganizationIdAndDeletedFalse(alertId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));

        alert.setAcknowledged(true);

        log.debug("Alert acknowledged: {}", alertId);
    }

    @Transactional
    public void deleteAlert(Long orgId, Long alertId) {

        orgValidationUtil.validateOrgAccess(orgId);
        featureGateService.checkAccess(Feature.BASIC_ALERTS);

        Alert alert = alertRepository
                .findByIdAndOrganizationIdAndDeletedFalse(alertId, orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert not found"));

        alert.setDeleted(true);

        log.debug("Alert soft deleted: {}", alertId);
    }

    private AlertResponse mapToResponse(Alert alert) {
        return AlertResponse.builder()
                .title(alert.getTitle())
                .message(alert.getMessage())
                .severity(alert.getSeverity())
                .acknowledged(alert.isAcknowledged())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}