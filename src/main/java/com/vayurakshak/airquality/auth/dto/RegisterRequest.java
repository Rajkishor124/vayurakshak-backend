package com.vayurakshak.airquality.auth.dto;

import com.vayurakshak.airquality.user.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "User registration request")
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Schema(example = "Rajkishor Murmu")
    private String name;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    @Schema(example = "raj@gmail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Password must contain at least one letter and one number"
    )
    @Schema(example = "Password123")
    private String password;

    @Schema(example = "ROLE_RESIDENT")
    private UserRole role;   // safer than String

    @NotNull(message = "Organization is required")
    @Schema(example = "1")
    private Long organizationId;
}