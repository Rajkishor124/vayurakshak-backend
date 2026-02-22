package com.vayurakshak.airquality.auth.controller;

import com.vayurakshak.airquality.auth.dto.AuthResponse;
import com.vayurakshak.airquality.auth.dto.LoginRequest;
import com.vayurakshak.airquality.auth.dto.RegisterRequest;
import com.vayurakshak.airquality.auth.service.AuthService;
import com.vayurakshak.airquality.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "User registration & login")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ApiResponse.success(
                "User registered successfully",
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ApiResponse.success(
                authService.login(request)
        );
    }
}