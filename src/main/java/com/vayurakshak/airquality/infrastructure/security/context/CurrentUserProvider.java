package com.vayurakshak.airquality.infrastructure.security.context;

import com.vayurakshak.airquality.infrastructure.exception.UnauthorizedException;
import com.vayurakshak.airquality.infrastructure.security.CustomUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    private CustomUserPrincipal getPrincipal() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || authentication.getPrincipal() == null
                || !(authentication.getPrincipal() instanceof CustomUserPrincipal)) {

            throw new UnauthorizedException("User not authenticated");
        }

        return (CustomUserPrincipal) authentication.getPrincipal();
    }

    public Long getCurrentOrganizationId() {
        return getPrincipal().getOrgId();
    }

    public String getCurrentRole() {
        return getPrincipal().getRole();
    }

    public String getCurrentSubscriptionPlan() {
        return getPrincipal().getSubscriptionPlan();
    }

    public String getCurrentEmail() {
        return getPrincipal().getUsername();
    }
}