package com.vayurakshak.airquality.report.service;

import com.vayurakshak.airquality.report.dto.HotspotResponse;
import com.vayurakshak.airquality.report.entity.PollutionReport;
import com.vayurakshak.airquality.report.repository.PollutionReportRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotspotService {

    private static final Logger log =
            LoggerFactory.getLogger(HotspotService.class);

    private final PollutionReportRepository reportRepository;

    public List<HotspotResponse> detectHotspots(Long orgId) {

        log.debug("Detecting hotspots for orgId={}", orgId);

        // fetch limited reports for aggregation (performance safe)
        var reportsPage = reportRepository.findByOrganizationId(
                orgId,
                PageRequest.of(0, 200)
        );

        List<PollutionReport> reports = reportsPage.getContent();

        Map<String, Long> groupedByLocation =
                reports.stream()
                        .collect(Collectors.groupingBy(
                                PollutionReport::getLocation,
                                Collectors.counting()
                        ));

        List<HotspotResponse> hotspots = groupedByLocation.entrySet()
                .stream()
                .map(entry -> HotspotResponse.builder()
                        .location(entry.getKey())
                        .reportCount(entry.getValue())
                        .isHotspot(entry.getValue() >= 3)
                        .build())
                .toList();

        log.debug("Hotspots detected for orgId={} count={}", orgId, hotspots.size());

        return hotspots;
    }
}
