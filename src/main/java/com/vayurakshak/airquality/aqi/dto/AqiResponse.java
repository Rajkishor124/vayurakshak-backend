package com.vayurakshak.airquality.aqi.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "AQI information response")
public class AqiResponse {

    @Schema(example = "Delhi")
    private String city;

    @Schema(example = "245")
    private int aqi;

    @Schema(example = "Very Poor")
    private String level;

    @Schema(example = "Air quality is Very Poor today")
    private String summary;
}
