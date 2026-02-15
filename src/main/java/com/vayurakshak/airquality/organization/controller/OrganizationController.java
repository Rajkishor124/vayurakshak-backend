package com.vayurakshak.airquality.organization.controller;

import com.vayurakshak.airquality.common.response.ApiResponse;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.service.OrganizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Organizations", description = "Organization onboarding & management")
@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService service;

    @PostMapping
    public ApiResponse<Organization> create(@Valid @RequestBody Organization org) {
        return ApiResponse.success("Organization created", service.create(org));
    }

    @GetMapping
    public ApiResponse<List<Organization>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse<Organization> getById(@PathVariable Long id) {
        return ApiResponse.success(service.getById(id));
    }
}
