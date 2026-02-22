package com.vayurakshak.airquality.organization.entity;

import com.vayurakshak.airquality.infrastructure.common.base.BaseEntity;
import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "organizations",
        indexes = {
                @Index(name = "idx_org_city", columnList = "city"),
                @Index(name = "idx_org_plan", columnList = "plan")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 50)
    private String type; // SOCIETY, CORPORATE, SCHOOL

    @Column(nullable = false, length = 100)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlan plan = SubscriptionPlan.FREE;
}