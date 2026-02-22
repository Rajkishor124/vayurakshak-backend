package com.vayurakshak.airquality.report.submission.dto;

import com.vayurakshak.airquality.report.enums.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Pollution report submission")
public class PollutionReportRequest {

    @NotNull(message = "Organization ID is required")
    private Long organizationId;

    @NotNull(message = "Report type required")
    @Schema(example = "GARBAGE_BURNING")
    private ReportType type;

    @NotNull(message = "Location required")
    @Schema(example = "Sector 5")
    private String location;

    @NotNull(message = "Description required")
    @Schema(example = "Plastic burning smoke detected")
    private String description;
}
