package com.vayurakshak.airquality.report.submission.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportResponse {

    private Long id;
    private String type;
    private String location;
    private String description;
    private LocalDateTime createdAt;
}
