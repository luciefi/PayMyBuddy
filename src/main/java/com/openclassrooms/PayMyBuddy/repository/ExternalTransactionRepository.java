package com.openclassrooms.PayMyBuddy.repository;

import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalTransactionRepository extends JpaRepository<ExternalTransaction, Long> {
    Page<ExternalTransaction> findByUserId(Long userId, Pageable pageable);
}
