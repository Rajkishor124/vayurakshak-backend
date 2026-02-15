package com.vayurakshak.airquality.advice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Personal health advice request")
public class AdviceRequest {

    @NotBlank(message = "City is required")
    @Schema(example = "Delhi")
    private String city;

    @Min(value = 1, message = "Age must be valid")
    @Schema(example = "30")
    private int age;

    @Schema(example = "true")
    private boolean hasRespiratoryIssue;
}
