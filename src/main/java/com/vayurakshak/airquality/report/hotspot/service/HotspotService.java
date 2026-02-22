package com.vayurakshak.airquality.report.hotspot.service;

import com.vayurakshak.airquality.infrastructure.security.OrgValidationUtil;
import com.vayurakshak.airquality.infrastructure.security.feature.Feature;
import com.vayurakshak.airquality.infrastructure.security.feature.FeatureGateService;
import com.vayurakshak.airquality.report.core.repository.PollutionReportRepository;
import com.vayurakshak.airquality.report.hotspot.dto.HotspotResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotspotService {

    private final PollutionReportRepository reportRepository;
    private final OrgValidationUtil orgValidationUtil;
    private final FeatureGateService featureGateService;

    @Transactional(readOnly = true)
    public List<HotspotResponse> detectHotspots(Long orgId) {

        // 🔐 Multi-tenant enforcement
        orgValidationUtil.validateOrgAccess(orgId);

        // 🎛 Subscription gating (Enterprise only)
        featureGateService.checkAccess(Feature.HOTSPOT_DETECTION);

        log.debug("Detecting hotspots for orgId={}", orgId);

        var aggregatedResults =
                reportRepository.countReportsByLocation(orgId);

        List<HotspotResponse> hotspots = aggregatedResults.stream()
                .map(row -> {
                    String location = (String) row[0];
                    Long count = (Long) row[1];

                    return HotspotResponse.builder()
                            .location(location)
                            .reportCount(count)
                            .isHotspot(count >= 3)
                            .build();
                })
                .toList();

        log.debug("Hotspots detected count={}", hotspots.size());

        return hotspots;
    }
}