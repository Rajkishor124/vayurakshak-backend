package com.vayurakshak.airquality.infrastructure.config;

import com.vayurakshak.airquality.aqi.entity.AqiRecord;
import com.vayurakshak.airquality.aqi.repository.AqiRepository;
import com.vayurakshak.airquality.organization.entity.Organization;
import com.vayurakshak.airquality.organization.enums.SubscriptionPlan;
import com.vayurakshak.airquality.organization.repository.OrganizationRepository;
import com.vayurakshak.airquality.user.entity.User;
import com.vayurakshak.airquality.user.enums.UserRole;
import com.vayurakshak.airquality.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final AqiRepository aqiRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Prevent duplicate seeding
        if (organizationRepository.count() > 0) {
            return;
        }

        // 1️⃣ Create Default Organization
        Organization org = organizationRepository.save(
                Organization.builder()
                        .name("Green Residency")
                        .type("SOCIETY")
                        .city("Delhi")
                        .plan(SubscriptionPlan.FREE)
                        .build()
        );

        // 2️⃣ Create Default Admin User
        userRepository.save(
                User.builder()
                        .name("Admin User")
                        .email("admin@green.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(UserRole.ROLE_ADMIN)
                        .organization(org)
                        .build()
        );

        // 3️⃣ Seed AQI Data
        aqiRepository.save(
                AqiRecord.builder()
                        .city("Delhi")
                        .aqi(382)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}