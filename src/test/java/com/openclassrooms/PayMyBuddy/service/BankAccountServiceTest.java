package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.configuration.WithMockCustomUser;
import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
@ExtendWith({SpringExtension.class,MockitoExtension.class})
@ContextConfiguration
@WithMockCustomUser
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    final Long BANK_ACCOUNT_ID_2 = Long.valueOf(2);
    final Long USER_ID_1 = Long.valueOf(1);

    @Test
    void getBankAccountTest() {
        // ARRANGE
        BankAccount ba = new BankAccount();
        when(bankAccountRepository.findById(BANK_ACCOUNT_ID_2)).thenReturn(Optional.of(ba));

        // ACT - ASSERT
        assertEquals(ba, bankAccountService.getById(BANK_ACCOUNT_ID_2));
        verify(bankAccountRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @WithMockCustomUser
    void getBankAccountsTest() {
        // ARRANGE
        when(bankAccountRepository.findByUserId(anyLong())).thenReturn(Collections.singletonList(new BankAccount()));

        // ACT - ASSERT
        List<BankAccount> bankAccountList = bankAccountService.getPaginatedForCurrentUser();
        assertEquals(1, bankAccountList.size());
        verify(bankAccountRepository, times(1)).findByUserId(anyLong());
    }

    @Test
    void deleteBankAccountTest() {
        // ARRANGE
        BankAccount bankAccount = new BankAccount();
        when(bankAccountRepository.findByIdAndUserId(anyLong(),anyLong())).thenReturn(Optional.of(bankAccount));
        // ACT - ASSERT
        bankAccountService.deleteBankAccount(BANK_ACCOUNT_ID_2);
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void saveBankAccountTest() {
        // ARRANGE
        when(userRepository.findById(USER_ID_1)).thenReturn(Optional.of(new User()));
        BankAccount ba = new BankAccount();
        ba.setId(BANK_ACCOUNT_ID_2);

        // ACT - ASSERT
        bankAccountService.saveBankAccount(ba);
        verify(userRepository, times(1)).findById(USER_ID_1);
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
        verify(bankAccountRepository, times(1)).findByIbanAndBicAndUserIdAndIdNot(any(),
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
        ba.setId(BANK_ACCOUNT_ID_2);

        // ACT - ASSERT
        assertThrows(BankAccountAlreadyExistsException.class,
                () -> bankAccountService.saveBankAccount(ba));
        verify(userRepository, times(0)).findById(USER_ID_1);
        verify(bankAccountRepository, times(0)).save(any(BankAccount.class));
    }

    @Test
    void saveBankAccountBankAccountIdNullTest() {
        // ARRANGE
        when(userRepository.findById(USER_ID_1)).thenReturn(Optional.of(new User()));
        BankAccount ba = new BankAccount();

        // ACT - ASSERT
        bankAccountService.saveBankAccount(ba);

        verify(userRepository, times(1)).findById(USER_ID_1);
        verify(bankAccountRepository, times(1)).findByIbanAndBicAndUserId(any(),
                any(), any(Long.class));
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void getAllActiveForCurrentUser() {
        // ARRANGE
        BankAccount ba = new BankAccount();
        ba.setId(BANK_ACCOUNT_ID_2);
        when(bankAccountRepository.findByUserIdAndDeactivated(anyLong(), anyBoolean())).thenReturn(Collections.singletonList(ba));

        // ACT
        List<BankAccount> bankAccountList = (List<BankAccount>) bankAccountService.getAllActiveForCurrentUser();

        // ASSERT
        verify(bankAccountRepository, times(1)).findByUserIdAndDeactivated(anyLong(), anyBoolean());
        assertEquals(1, bankAccountList.size());
    }

    @Test
    void activateBankAccount() {
        // ARRANGE
        BankAccount ba = new BankAccount();
        ba.setId(BANK_ACCOUNT_ID_2);
        when(bankAccountRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(ba));

        // ACT
        bankAccountService.activateBankAccount(1L);

        // ASSERT
        verify(bankAccountRepository, times(1)).findByIdAndUserId(anyLong(), anyLong());
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void activateBankAccountUserNotFound() {
        // ARRANGE
        when(bankAccountRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // ACT
        assertThrows(BankAccountNotFoundException.class, () -> bankAccountService.activateBankAccount(1L));

        // ASSERT
        verify(bankAccountRepository, times(1)).findByIdAndUserId(anyLong(), anyLong());
        verify(bankAccountRepository, never()).save(any(BankAccount.class));
    }
}