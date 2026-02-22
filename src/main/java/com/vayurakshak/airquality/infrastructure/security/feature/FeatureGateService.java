package com.vayurakshak.airquality.infrastructure.security.feature;

import com.vayurakshak.airquality.infrastructure.exception.UnauthorizedException;
import com.vayurakshak.airquality.infrastructure.security.context.CurrentUserProvider;
import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeatureGateService {

    private final CurrentUserProvider currentUserProvider;

    public void checkAccess(Feature feature) {

        String planString = currentUserProvider.getCurrentSubscriptionPlan();

        SubscriptionPlan plan =
                SubscriptionPlan.valueOf(planString);

        boolean allowed =
                PlanFeatureMatrix.isFeatureAllowed(plan, feature);

        if (!allowed) {
            throw new UnauthorizedException(
                    "Upgrade your subscription plan to access this feature"
            );
        }
    }
}