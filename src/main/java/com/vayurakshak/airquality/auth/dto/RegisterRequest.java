package com.vayurakshak.airquality.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User registration request")
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Schema(example = "Rajkishor Murmu")
    private String name;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    @Schema(example = "raj@gmail.com")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(example = "password123")
    private String password;

    @Schema(example = "ROLE_RESIDENT")
    private String role;

    @NotNull(message = "Organization is required")
    @Schema(example = "1")
    private Long organizationId;
}
