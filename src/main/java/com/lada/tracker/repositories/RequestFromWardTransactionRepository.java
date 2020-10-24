package com.lada.tracker.repositories;

import com.lada.tracker.entities.RequestFromWardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestFromWardTransactionRepository extends JpaRepository<RequestFromWardTransaction, Integer> {

    Optional<RequestFromWardTransaction> findByFromAndTo(int from, int to);

}
