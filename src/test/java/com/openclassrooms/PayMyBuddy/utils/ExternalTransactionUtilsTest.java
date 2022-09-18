package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import com.openclassrooms.PayMyBuddy.model.TransactionType;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class ExternalTransactionUtilsTest {

    @Test
    void convertToExternalTransaction() {
        // ARRANGE
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setTransactionType("0");
        externalTransactionDto.setAmount(1d);
        externalTransactionDto.setDescription("test");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setName("my bank account");

        // ACT
        ExternalTransaction externalTransaction = ExternalTransactionUtils.convertToExternalTransaction(externalTransactionDto, bankAccount);

        // ASSERT
        assertEquals(externalTransactionDto.getTransactionType(), externalTransaction.getTransactionType().getValue() + "");
        assertEquals(externalTransactionDto.getAmount(), externalTransaction.getAmount());
        assertEquals(externalTransactionDto.getDescription(), externalTransaction.getDescription());
        assertEquals(bankAccount.getId(), externalTransaction.getBankAccount().getId());
        assertEquals(bankAccount.getName(), externalTransaction.getBankAccount().getName());
    }

    @Test
    void convertToExternalTransactionDto() {
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

        // ACT
        ExternalTransactionDto externalTransactionDto = ExternalTransactionUtils.convertToExternalTransactionDto(externalTransaction);

        // ASSERT
        assertEquals(externalTransaction.getTransactionType().getValue() + "", externalTransactionDto.getTransactionType());
        assertEquals(externalTransaction.getAmount(), externalTransactionDto.getAmount());
        assertEquals(externalTransaction.getDescription(), externalTransactionDto.getDescription());
        assertEquals(externalTransaction.getBankAccount().getId(), externalTransactionDto.getBankAccountId());
        assertEquals(externalTransaction.getBankAccount().getId(), externalTransactionDto.getBankAccount().getId());
        assertEquals(externalTransaction.getBankAccount().getName(), externalTransactionDto.getBankAccount().getName());
        assertEquals(externalTransaction.getTimestamp(), externalTransactionDto.getTimestamp());
    }

    @Test
    void convertToExternalTransactionDtoDebit() {
        // ARRANGE
        ExternalTransaction externalTransaction = new ExternalTransaction();
        externalTransaction.setTransactionType(TransactionType.DEBIT_EXTERNAL_ACCOUNT);
        externalTransaction.setAmount(1d);
        externalTransaction.setDescription("test");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setName("my bank account");
        externalTransaction.setBankAccount(bankAccount);
        externalTransaction.setTimestamp(new Timestamp(0));

        // ACT
        ExternalTransactionDto externalTransactionDto = ExternalTransactionUtils.convertToExternalTransactionDto(externalTransaction);

        // ASSERT
        assertEquals(externalTransaction.getAmount(), externalTransactionDto.getAmount());
    }

}