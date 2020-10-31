package com.lada.tracker.repositories;

import com.lada.tracker.entities.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestTypeRepository extends JpaRepository<RequestType, Long> {
}
