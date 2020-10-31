package com.lada.tracker.repositories;

import com.lada.tracker.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByStatus(int status);

}