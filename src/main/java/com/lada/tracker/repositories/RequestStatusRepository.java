package com.lada.tracker.repositories;

import com.lada.tracker.entities.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestStatusRepository extends JpaRepository<RequestStatus, Integer> {

}
