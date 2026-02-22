package com.vayurakshak.airquality.infrastructure.security.feature;

import com.vayurakshak.airquality.infrastructure.exception.UnauthorizedException;
import com.vayurakshak.airquality.infrastructure.security.context.CurrentUserProvider;
import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeatureGateService {

    private final CurrentUserProvider currentUserProvider;

    private static final Map<SubscriptionPlan, Set<Feature>> PLAN_FEATURES =
            new EnumMap<>(SubscriptionPlan.class);

    static {

        PLAN_FEATURES.put(SubscriptionPlan.FREE,
                EnumSet.of(
                        Feature.REPORT_SUBMISSION,
                        Feature.REPORT_VIEW,
                        Feature.BASIC_ALERTS
                ));

        PLAN_FEATURES.put(SubscriptionPlan.PRO,
                EnumSet.of(
                        Feature.REPORT_SUBMISSION,
                        Feature.REPORT_VIEW,
                        Feature.REPORT_DELETE,
                        Feature.BASIC_ALERTS,
                        Feature.SMART_ALERTS,
                        Feature.DASHBOARD_ANALYTICS,
                        Feature.HOTSPOT_DETECTION
                ));

        PLAN_FEATURES.put(SubscriptionPlan.ENTERPRISE,
                EnumSet.allOf(Feature.class));
    }

    public void checkAccess(Feature feature) {

        String planName = currentUserProvider.getCurrentSubscriptionPlan();

        if (planName == null) {
            throw new UnauthorizedException("Subscription plan not found");
        }

        SubscriptionPlan plan = SubscriptionPlan.valueOf(planName);

        Set<Feature> allowedFeatures =
                PLAN_FEATURES.getOrDefault(plan, EnumSet.noneOf(Feature.class));

        if (!allowedFeatures.contains(feature)) {
            throw new UnauthorizedException(
                    "Upgrade required to access: " + feature.name()
            );
        }
    }
}