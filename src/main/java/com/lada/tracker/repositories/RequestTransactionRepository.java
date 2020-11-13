package com.lada.tracker.repositories;

import com.lada.tracker.entities.RequestTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestTransactionRepository extends JpaRepository<RequestTransaction, Integer> {

    Optional<RequestTransaction> findByFromAndTo(int from, int to);

}
