package com.openclassrooms.PayMyBuddy.repository;

import com.openclassrooms.PayMyBuddy.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByIbanAndBicAndUserIdAndIdNot(String iban, String bic, Long currentUser, Long id);

    List<BankAccount> findByIbanAndBicAndUserId(String iban, String bic, Long currentUser);

    Page<BankAccount> findByUserId(Long currentUserId, Pageable pageable);

    Iterable<BankAccount> findByUserIdAndDeactivated(Long currentUserId, boolean deactivated);

    Optional<BankAccount> findByIdAndUserId(Long id, Long userId);
}
