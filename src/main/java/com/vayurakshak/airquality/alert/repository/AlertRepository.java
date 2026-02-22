package com.vayurakshak.airquality.alert.repository;

import com.vayurakshak.airquality.alert.entity.Alert;
import com.vayurakshak.airquality.organization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    Page<Alert> findByOrganizationIdAndDeletedFalse(
            Long organizationId,
            Pageable pageable
    );

    Optional<Alert> findByIdAndOrganizationIdAndDeletedFalse(
            Long id,
            Long organizationId
    );
}