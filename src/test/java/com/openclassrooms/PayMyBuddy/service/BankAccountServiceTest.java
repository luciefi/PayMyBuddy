package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    final Long BANK_ACCOUNT_ID_1 = Long.valueOf(1);
    final Long USER_ID_1 = Long.valueOf(1);

    @Test
    void getBankAccountTest() {
        // ARRANGE
        BankAccount ba = new BankAccount();
        when(bankAccountRepository.findById(BANK_ACCOUNT_ID_1)).thenReturn(Optional.of(ba));

        // ACT - ASSERT
        assertEquals(ba, bankAccountService.getById(BANK_ACCOUNT_ID_1));
        verify(bankAccountRepository, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    void getBankAccountsTest() {
        // ARRANGE
        when(bankAccountRepository.findByUserId(anyLong())).thenReturn(Collections.singletonList(new BankAccount()));

        // ACT - ASSERT
        List<BankAccount> bankAccountList = bankAccountService.getAllForCurrentUser();
        assertEquals(1, bankAccountList.size());
        verify(bankAccountRepository, Mockito.times(1)).findByUserId(anyLong());
    }

    @Test
    void deleteBankAccountTest() {
        // ACT - ASSERT
        bankAccountService.deleteBankAccount(BANK_ACCOUNT_ID_1);
        verify(bankAccountRepository, Mockito.times(1)).deleteById(any(Long.class));
    }

    @Test
    void saveBankAccountTest() {
        // ARRANGE
        when(userRepository.findById(USER_ID_1)).thenReturn(Optional.of(new User()));
        BankAccount ba = new BankAccount();
        ba.setId(BANK_ACCOUNT_ID_1);

        // ACT - ASSERT
        bankAccountService.saveBankAccount(ba);
        //TODO mock current user utils ??
        verify(userRepository, Mockito.times(1)).findById(USER_ID_1);
        verify(bankAccountRepository, Mockito.times(1)).save(any(BankAccount.class));
        verify(bankAccountRepository, Mockito.times(1)).findByIbanAndBicAndUserIdAndIdNot(any(),
                any(), any(Long.class), any(Long.class));
    }

    @Test
    void saveBankAccountAlreadyExistsTest() {
        // ARRANGE
        List<BankAccount> bankAccountList = new ArrayList<>();
        bankAccountList.add(new BankAccount());
        when(bankAccountRepository.findByIbanAndBicAndUserIdAndIdNot(any(), any(),
                any(Long.class), any(Long.class))).thenReturn(bankAccountList);


        BankAccount ba = new BankAccount();
        ba.setId(BANK_ACCOUNT_ID_1);

        // ACT - ASSERT
        assertThrows(BankAccountAlreadyExistsException.class,
                () -> bankAccountService.saveBankAccount(ba));
        //TODO mock current user utils ??
        verify(userRepository, Mockito.times(0)).findById(USER_ID_1);
        verify(bankAccountRepository, Mockito.times(0)).save(any(BankAccount.class));
    }

    @Test
    void saveBankAccountBankAccountIdNullTest() {
        // ARRANGE
        when(userRepository.findById(USER_ID_1)).thenReturn(Optional.of(new User()));
        BankAccount ba = new BankAccount();

        // ACT - ASSERT
        bankAccountService.saveBankAccount(ba);

        //TODO mock current user utils ??
        verify(userRepository, Mockito.times(1)).findById(USER_ID_1);
        verify(bankAccountRepository, Mockito.times(1)).findByIbanAndBicAndUserId(any(),
                any(), any(Long.class));
        verify(bankAccountRepository, Mockito.times(1)).save(any(BankAccount.class));
    }
}