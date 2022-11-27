package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.BankAccount;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBankAccountService {
    BankAccount getById(Long id);

    Page<BankAccount> getPaginatedForCurrentUser(int pageNumber);

    void deleteBankAccount(Long id);

    BankAccount saveBankAccount(BankAccount bankAccount);

    Iterable<BankAccount> getAllActiveForCurrentUser();

    void activateBankAccount(Long id);
}
