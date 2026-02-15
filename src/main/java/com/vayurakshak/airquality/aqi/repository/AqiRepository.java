package com.vayurakshak.airquality.aqi.repository;

import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AqiRepository extends JpaRepository<AqiRecord, Long> {

    Optional<AqiRecord> findTopByCityOrderByTimestampDesc(String city);
}
