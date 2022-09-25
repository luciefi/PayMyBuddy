package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.*;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public double getBalance() {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        return user.getBalance();
    }

    @Override
    public void updateBalance(Double amount, TransactionType type) throws InsufficientBalanceException {

        if (type.equals(TransactionType.CREDIT_EXTERNAL_ACCOUNT)) {
            debitBalance(amount);
        } else {
            creditBalance(amount, CurrentUserUtils.getCurrentUserId());
        }
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void debitBalance(double amountWithCommission) throws InsufficientBalanceException {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        double balance = user.getBalance();
        if (balance < amountWithCommission) {
            throw new InsufficientBalanceException();
        }
        user.setBalance(balance - amountWithCommission);
        userRepository.save(user);
    }

    @Override
    public void creditBalance(Double amount, Long recipientId) {
        User user = userRepository.findById(recipientId).orElseThrow(UserNotFoundException::new);
        Double balance = user.getBalance();
        user.setBalance(balance + amount);
        userRepository.save(user);
    }

}
