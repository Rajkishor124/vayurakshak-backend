package com.vayurakshak.airquality.organization.service;

import com.vayurakshak.airquality.common.exception.ResourceNotFoundException;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private static final Logger log =
            LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository repository;

    public Organization create(Organization org) {

        log.info("Creating organization: name={}, city={}, plan={}",
                org.getName(),
                org.getCity(),
                org.getPlan());

        Organization saved = repository.save(org);

        log.info("Organization created successfully with id={}", saved.getId());

        return saved;
    }

    public Organization getById(Long id) {

        log.debug("Fetching organization with id={}", id);

        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Organization not found with id={}", id);
                    return new ResourceNotFoundException("Organization not found");
                });
    }

    public List<Organization> getAll() {

        log.debug("Fetching all organizations");

        List<Organization> organizations = repository.findAll();

        log.debug("Total organizations found={}", organizations.size());

        return organizations;
    }
}
