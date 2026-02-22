package com.vayurakshak.airquality.infrastructure.security.feature;

import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;

import java.util.Map;
import java.util.Set;

public class PlanFeatureMatrix {

    private static final Map<SubscriptionPlan, Set<Feature>> PLAN_FEATURES = Map.of(

            SubscriptionPlan.FREE,
            Set.of(Feature.BASIC_ALERTS),

            SubscriptionPlan.PRO,
            Set.of(
                    Feature.BASIC_ALERTS,
                    Feature.SMART_ALERTS,
                    Feature.DASHBOARD_ANALYTICS
            ),

            SubscriptionPlan.ENTERPRISE,
            Set.of(
                    Feature.BASIC_ALERTS,
                    Feature.SMART_ALERTS,
                    Feature.DASHBOARD_ANALYTICS,
                    Feature.HOTSPOT_DETECTION,
                    Feature.ADMIN_REPORTS
            )
    );

    public static boolean isFeatureAllowed(SubscriptionPlan plan, Feature feature) {
        return PLAN_FEATURES
                .getOrDefault(plan, Set.of())
                .contains(feature);
    }
}