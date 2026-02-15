package com.vayurakshak.airquality.alert.entity;

import com.vayurakshak.airquality.organization.entity.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 500)
    private String message;

    private String severity; // INFO, MEDIUM, HIGH, CRITICAL

    private boolean acknowledged; // user has seen alert

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.acknowledged = false;
    }
}
