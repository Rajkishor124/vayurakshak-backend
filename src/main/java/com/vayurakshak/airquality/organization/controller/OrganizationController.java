package com.vayurakshak.airquality.organization.controller;

import com.vayurakshak.airquality.organization.dto.OrganizationResponse;
import com.vayurakshak.airquality.shared.response.ApiResponse;
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
    public ApiResponse<OrganizationResponse> create(
            @Valid @RequestBody Organization org) {

        Organization saved = service.create(org);

        return ApiResponse.success(
                "Organization created",
                map(saved)
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<OrganizationResponse> getById(@PathVariable Long id) {

        return ApiResponse.success(map(service.getById(id)));
    }

    private OrganizationResponse map(Organization org) {
        return OrganizationResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .type(org.getType())
                .city(org.getCity())
                .plan(org.getPlan())
                .build();
    }
}