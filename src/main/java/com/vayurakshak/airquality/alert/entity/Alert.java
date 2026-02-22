package com.vayurakshak.airquality.alert.entity;

import com.vayurakshak.airquality.alert.enums.AlertSeverity;
import com.vayurakshak.airquality.infrastructure.common.base.BaseEntity;
import com.vayurakshak.airquality.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    @Column(nullable = false)
    private boolean acknowledged = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
}