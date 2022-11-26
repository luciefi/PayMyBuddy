package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.BankAccount;

import java.util.List;

public interface IBankAccountService {
    BankAccount getById(Long id);

    List<BankAccount> getPaginatedForCurrentUser();

    void deleteBankAccount(Long id);

    BankAccount saveBankAccount(BankAccount bankAccount);

    Iterable<BankAccount> getAllActiveForCurrentUser();

    void activateBankAccount(Long id);
}
