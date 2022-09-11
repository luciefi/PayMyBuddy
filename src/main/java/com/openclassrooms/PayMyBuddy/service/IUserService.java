package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.EmailAddress;
import com.openclassrooms.PayMyBuddy.model.TransactionType;
import com.openclassrooms.PayMyBuddy.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUser(Long id);

    Iterable<User> getUsers();

    void deleteUser(Long id);

    float getBalance();

    void updateBalance(Float amount, TransactionType type) throws InsufficientBalanceException;
}
