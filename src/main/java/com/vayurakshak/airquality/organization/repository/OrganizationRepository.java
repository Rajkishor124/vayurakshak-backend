package com.vayurakshak.airquality.organization.repository;

import com.vayurakshak.airquality.organization.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository
        extends JpaRepository<Organization, Long> {
}
