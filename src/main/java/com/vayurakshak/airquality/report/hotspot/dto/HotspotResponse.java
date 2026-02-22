package com.vayurakshak.airquality.report.hotspot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HotspotResponse {

    private String location;
    private long reportCount;
    private boolean isHotspot;
}
