package com.vayurakshak.airquality.report.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminSummary {

    private Long totalReports;
    private String message;
}
