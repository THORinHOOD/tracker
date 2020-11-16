package com.lada.tracker.repositories;

import com.lada.tracker.entities.RequestRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRoleRepository extends JpaRepository<RequestRole, String> {
    Optional<String> findByRusName(String name);
}
