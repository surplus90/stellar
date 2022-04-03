package com.ponyz.stellar.domain.admin.Repository;

import com.ponyz.stellar.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByIdAndPassword(String id, String password);
}
