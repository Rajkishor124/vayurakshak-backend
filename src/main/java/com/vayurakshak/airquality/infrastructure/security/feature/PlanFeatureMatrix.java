package com.vayurakshak.airquality.infrastructure.security.feature;

import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public final class PlanFeatureMatrix {

    private PlanFeatureMatrix() {
    }

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

    public static boolean isFeatureAllowed(
            SubscriptionPlan plan,
            Feature feature
    ) {
        return PLAN_FEATURES
                .getOrDefault(plan, EnumSet.noneOf(Feature.class))
                .contains(feature);
    }
}