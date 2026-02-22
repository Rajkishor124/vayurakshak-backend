package com.vayurakshak.airquality.infrastructure.security;

import com.vayurakshak.airquality.infrastructure.exception.UnauthorizedException;
import com.vayurakshak.airquality.infrastructure.security.context.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrgValidationUtil {

    private final CurrentUserProvider currentUserProvider;

    public void validateOrgAccess(Long pathOrgId) {

        Long jwtOrgId = currentUserProvider.getCurrentOrganizationId();

        if (jwtOrgId == null || !jwtOrgId.equals(pathOrgId)) {
            throw new UnauthorizedException("Cross-organization access denied");
        }
    }
}