package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.TransactionType;
import com.openclassrooms.PayMyBuddy.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUser(Long id);

    Iterable<User> getUsers();

    void deleteUser(Long id);

    double getBalance();

    void updateBalance(Double amount, TransactionType type) throws InsufficientBalanceException;

    User saveUser(User user);

    void debitBalance(double v) throws InsufficientBalanceException;

    void creditBalance(Double amount, Long recipientId) throws InsufficientBalanceException;

    User getCurrentUser();
}
