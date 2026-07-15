package com.navora.backend.repository;

import com.navora.backend.entity.Scan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ScanRepository extends JpaRepository<Scan, UUID> {

    List<Scan> findByUserId(UUID userId);
}
