package com.vayurakshak.airquality.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User login request")
public class LoginRequest {

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    @Schema(example = "raj@gmail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(example = "password123")
    private String password;
}
