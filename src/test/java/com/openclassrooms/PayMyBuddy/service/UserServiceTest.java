package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.TransactionType;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getBalance() {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT - ASSERT
        assertEquals(10f, userService.getBalance());
        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void getBalanceUnknownUser() {
        // ARRANGE
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT - ASSERT
        assertThrows(UserNotFoundException.class, () -> userService.getBalance());
        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void updateBalanceCredit() throws InsufficientBalanceException {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT
        userService.updateBalance(5d, TransactionType.DEBIT_EXTERNAL_ACCOUNT);

        // ASSERT
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        assertEquals(15f, user.getBalance());
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }


    @Test
    void updateBalanceDebit() throws InsufficientBalanceException {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT
        userService.updateBalance(5d, TransactionType.CREDIT_EXTERNAL_ACCOUNT);

        // ASSERT
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        assertEquals(5f, user.getBalance());
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    void updateBalanceInsufficientBalance() throws InsufficientBalanceException {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT
        assertThrows(InsufficientBalanceException.class, () -> userService.updateBalance(50d, TransactionType.CREDIT_EXTERNAL_ACCOUNT));

        // ASSERT
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        assertEquals(10f, user.getBalance());
        verify(userRepository, Mockito.times(0)).save(any(User.class));
    }

    @Test
    void updateBalanceUnknownUser() {
        // ARRANGE
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT - ASSERT
        assertThrows(UserNotFoundException.class, () -> userService.updateBalance(1d, TransactionType.CREDIT_EXTERNAL_ACCOUNT));
        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void saveUser() {
    }
}