package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.*;

import java.util.Optional;

public interface IUserService {
    Optional<User> getUser(Long id);

    Iterable<User> getUsers();

    void deleteUser(Long id);

    double getBalance();

    void updateBalance(Double amount, TransactionType type) throws InsufficientBalanceException;

    User saveNewUser(ProfileDto profileDto);

    User updateUser(UserDto userDto);

    void debitBalance(double v) throws InsufficientBalanceException;

    void creditBalance(Double amount, Long recipientId) throws InsufficientBalanceException;

    UserDto getCurrentUserDto();

    void logUserIn(LoginDto loginDto);

    void updatePassword(PasswordUpdateDto passwordUpdateDto);
}
