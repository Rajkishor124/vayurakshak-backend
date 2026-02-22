package com.vayurakshak.airquality.organization.service;

import com.vayurakshak.airquality.infrastructure.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.infrastructure.exception.UnauthorizedException;
import com.vayurakshak.airquality.infrastructure.security.OrgValidationUtil;
import com.vayurakshak.airquality.infrastructure.security.context.CurrentUserProvider;
import com.vayurakshak.airquality.infrastructure.security.feature.FeatureGateService;
import com.vayurakshak.airquality.infrastructure.security.JwtService;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.user.entity.User;
import com.vayurakshak.airquality.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository repository;
    private final OrgValidationUtil orgValidationUtil;
    private final CurrentUserProvider currentUserProvider;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Transactional
    public Organization create(Organization org) {

        if (org.getPlan() == null) {
            org.setPlan(SubscriptionPlan.FREE);
        }

        return repository.save(org);
    }

    @Transactional(readOnly = true)
    public Organization getById(Long orgId) {

        orgValidationUtil.validateOrgAccess(orgId);

        return repository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));
    }

    @Transactional(readOnly = true)
    public List<Organization> getAll() {

        if (!"ROLE_ADMIN".equals(currentUserProvider.getCurrentRole())) {
            throw new UnauthorizedException("Admin access required");
        }

        return repository.findAll()
                .stream()
                .filter(org -> !org.isDeleted())
                .toList();
    }

    @Transactional
    public String upgradePlan(Long orgId, SubscriptionPlan newPlan) {

        orgValidationUtil.validateOrgAccess(orgId);

        if (!"ROLE_ADMIN".equals(currentUserProvider.getCurrentRole())) {
            throw new UnauthorizedException("Only admin can upgrade plan");
        }

        Organization organization = repository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        organization.setPlan(newPlan);

        log.info("Organization {} upgraded to {}", orgId, newPlan);

        // 🔄 Reload user from DB to generate updated JWT
        String email = currentUserProvider.getCurrentEmail();

        User user = userRepository
                .findByEmailIgnoreCaseAndDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return jwtService.generateToken(user);
    }
}