package com.vayurakshak.airquality.organization.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type; // SOCIETY, CORPORATE, SCHOOL

    private String city;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan plan;
}
