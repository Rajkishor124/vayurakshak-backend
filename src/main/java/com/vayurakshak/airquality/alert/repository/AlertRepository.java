package com.vayurakshak.airquality.alert.repository;

import com.vayurakshak.airquality.alert.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
