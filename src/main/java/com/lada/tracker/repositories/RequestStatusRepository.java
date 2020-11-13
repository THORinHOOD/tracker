package com.lada.tracker.repositories;

import com.lada.tracker.entities.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {

}
