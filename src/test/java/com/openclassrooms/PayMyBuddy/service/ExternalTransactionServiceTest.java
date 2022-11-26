package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.configuration.WithMockCustomUser;
import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import com.openclassrooms.PayMyBuddy.model.TransactionType;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;
import com.openclassrooms.PayMyBuddy.repository.ExternalTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class,MockitoExtension.class})
@ContextConfiguration
@WithMockCustomUser
class ExternalTransactionServiceTest {

    @Mock
    private ExternalTransactionRepository externalTransactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExternalTransactionService externalTransactionService;

    @Test
    void saveExternalTransaction() throws InsufficientBalanceException {
        // ARRANGE
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setName("my bank account");
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.of(bankAccount));

        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setAmount(1d);
        externalTransactionDto.setTransactionType("1");
        externalTransactionDto.setDescription("test");
        externalTransactionDto.setBankAccountId(2L);

        // ACT
        externalTransactionService.saveExternalTransaction(externalTransactionDto);

        // ASSERT
        verify(bankAccountRepository, Mockito.times(1)).findById(any(Long.class));
        verify(userService, Mockito.times(1)).updateBalance(any(Double.class), any(TransactionType.class));
        verify(externalTransactionRepository, Mockito.times(1)).save(any(ExternalTransaction.class));
    }

    @Test
    void saveExternalTransactionUnknownBankAccount() throws InsufficientBalanceException {
        // ARRANGE
        when(bankAccountRepository.findById(anyLong())).thenReturn(Optional.empty());
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setBankAccountId(2L);

        // ACT
        assertThrows(BankAccountNotFoundException.class, () -> externalTransactionService.saveExternalTransaction(externalTransactionDto));

        // ASSERT
        verify(bankAccountRepository, Mockito.times(1)).findById(any(Long.class));
        verify(userService, Mockito.never()).updateBalance(any(Double.class), any(TransactionType.class));
        verify(externalTransactionRepository, Mockito.never()).save(any(ExternalTransaction.class));
    }

    @Test
    void getAll() {
        // ARRANGE
        ExternalTransaction externalTransaction = new ExternalTransaction();
        when(externalTransactionRepository.findByUserId(anyLong())).thenReturn(Collections.singletonList(externalTransaction));

        // ACT
        List<ExternalTransaction> externalTransactionList = externalTransactionService.getAll();

        // ASSERT
        assertEquals(1, externalTransactionList.size());
        assertEquals(externalTransaction, externalTransactionList.get(0));
        verify(externalTransactionRepository, Mockito.times(1)).findByUserId(any(Long.class));
    }

    @Test
    void getAllDto() {
        // ARRANGE
        ExternalTransaction externalTransaction = new ExternalTransaction();
        externalTransaction.setTransactionType(TransactionType.CREDIT_EXTERNAL_ACCOUNT);
        externalTransaction.setAmount(1d);
        externalTransaction.setDescription("test");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setName("my bank account");
        externalTransaction.setBankAccount(bankAccount);
        externalTransaction.setTimestamp(new Timestamp(0));
        when(externalTransactionRepository.findByUserId(anyLong())).thenReturn(Collections.singletonList(externalTransaction));

        // ACT
        List<ExternalTransactionDto> externalTransactionDtos = externalTransactionService.getAllDto();

        // ASSERT
        assertEquals(1, externalTransactionDtos.size());
        assertEquals(externalTransaction.getTransactionType().getValue() + "", externalTransactionDtos.get(0).getTransactionType());
        assertEquals(externalTransaction.getAmount(), externalTransactionDtos.get(0).getAmount());
        assertEquals(externalTransaction.getDescription(), externalTransactionDtos.get(0).getDescription());
        assertEquals(externalTransaction.getBankAccount().getId(), externalTransactionDtos.get(0).getBankAccountId());
        assertEquals(externalTransaction.getBankAccount().getId(), externalTransactionDtos.get(0).getBankAccount().getId());
        assertEquals(externalTransaction.getBankAccount().getName(), externalTransactionDtos.get(0).getBankAccount().getName());
        assertEquals(externalTransaction.getTimestamp(), externalTransactionDtos.get(0).getTimestamp());
        verify(externalTransactionRepository, Mockito.times(1)).findByUserId(any(Long.class));
    }
}