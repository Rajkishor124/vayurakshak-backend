package com.vayurakshak.airquality.report.repository;

import com.vayurakshak.airquality.report.entity.PollutionReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollutionReportRepository extends JpaRepository<PollutionReport, Long> {

    Page<PollutionReport> findByOrganizationId(Long organizationId, Pageable pageable);
}
