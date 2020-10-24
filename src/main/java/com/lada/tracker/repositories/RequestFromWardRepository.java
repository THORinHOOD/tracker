package com.lada.tracker.repositories;

import com.lada.tracker.entities.RequestFromWard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestFromWardRepository extends JpaRepository<RequestFromWard, Long> {

    List<RequestFromWard> findAllByStatus(int status);

}