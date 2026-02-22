package com.vayurakshak.airquality.user.repository;

import com.vayurakshak.airquality.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔐 Soft-delete safe login
    Optional<User> findByEmailIgnoreCaseAndDeletedFalse(String email);

    // 🔎 Existence check (soft-delete safe)
    boolean existsByEmailIgnoreCaseAndDeletedFalse(String email);

    // 🔐 Secure user fetch within organization
    Optional<User> findByIdAndOrganizationIdAndDeletedFalse(
            Long userId,
            Long organizationId
    );
}