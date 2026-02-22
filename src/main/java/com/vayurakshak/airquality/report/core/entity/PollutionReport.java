package com.vayurakshak.airquality.report.core.entity;

import com.vayurakshak.airquality.infrastructure.common.base.BaseEntity;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.report.enums.ReportType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "pollution_reports",
        indexes = {
                @Index(name = "idx_report_org", columnList = "organization_id"),
                @Index(name = "idx_report_type", columnList = "type"),
                @Index(name = "idx_report_location", columnList = "location"),
                @Index(name = "idx_report_created_at", columnList = "createdAt")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollutionReport extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportType type;

    @Column(nullable = false, length = 150)
    private String location;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
}