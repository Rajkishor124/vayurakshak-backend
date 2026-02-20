package com.vayurakshak.airquality.infrastructure.config;

import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.repository.AqiRepository;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.entity.SubscriptionPlan;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AqiRepository aqiRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public void run(String... args) {

        // 1️⃣ Create Default Organization (Tenant)
        Organization org = organizationRepository.save(
                Organization.builder()
                        .name("Green Residency")
                        .type("SOCIETY")
                        .city("Delhi")
                        .plan(SubscriptionPlan.FREE)
                        .build()
        );

        // 2️⃣ Seed AQI Data
        aqiRepository.save(
                AqiRecord.builder()
                        .city("Delhi")
                        .aqi(382)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
