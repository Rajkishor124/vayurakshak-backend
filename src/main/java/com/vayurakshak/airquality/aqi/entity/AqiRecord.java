package com.vayurakshak.airquality.aqi.entity;

import com.vayurakshak.airquality.infrastructure.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "aqi_records",
        indexes = {
                @Index(name = "idx_aqi_city", columnList = "city"),
                @Index(name = "idx_aqi_city_timestamp", columnList = "city,timestamp")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AqiRecord extends BaseEntity {

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private int aqi;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}