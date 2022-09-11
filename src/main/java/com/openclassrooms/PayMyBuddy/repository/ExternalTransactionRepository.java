package com.openclassrooms.PayMyBuddy.repository;

import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ExternalTransactionRepository extends JpaRepository<ExternalTransaction, Long> {
    Iterable<ExternalTransaction> findByUserId(Long userId);
}
