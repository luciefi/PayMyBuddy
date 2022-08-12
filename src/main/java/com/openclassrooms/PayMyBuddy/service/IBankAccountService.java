package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.BankAccount;

import java.util.List;

public interface IBankAccountService {
    BankAccount getById(Long id);

    List<BankAccount> getAll();

    void deleteBankAccount(Long id);

    BankAccount saveBankAccount(BankAccount bankAccount);
}
