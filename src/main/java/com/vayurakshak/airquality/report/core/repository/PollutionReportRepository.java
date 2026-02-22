package com.vayurakshak.airquality.report.core.repository;

import com.vayurakshak.airquality.report.core.entity.PollutionReport;
import com.vayurakshak.airquality.report.enums.ReportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PollutionReportRepository
        extends JpaRepository<PollutionReport, Long> {

    Page<PollutionReport> findByOrganizationIdAndDeletedFalse(
            Long organizationId,
            Pageable pageable
    );

    Optional<PollutionReport> findByIdAndOrganizationIdAndDeletedFalse(
            Long id,
            Long organizationId
    );

    Page<PollutionReport> findByOrganizationIdAndTypeAndDeletedFalse(
            Long organizationId,
            ReportType type,
            Pageable pageable
    );

    long countByOrganizationIdAndDeletedFalse(Long organizationId);

    boolean existsByIdAndOrganizationIdAndDeletedFalse(
            Long id,
            Long organizationId
    );

    // 🔥 Optimized DB aggregation for dashboard
    @Query("""
        SELECT r.type, COUNT(r)
        FROM PollutionReport r
        WHERE r.organization.id = :orgId
          AND r.deleted = false
        GROUP BY r.type
    """)
    List<Object[]> countReportsByType(Long orgId);

    // 🔥 Optimized DB aggregation for hotspot
    @Query("""
        SELECT r.location, COUNT(r)
        FROM PollutionReport r
        WHERE r.organization.id = :orgId
          AND r.deleted = false
        GROUP BY r.location
        ORDER BY COUNT(r) DESC
    """)
    List<Object[]> countReportsByLocation(Long orgId);
}