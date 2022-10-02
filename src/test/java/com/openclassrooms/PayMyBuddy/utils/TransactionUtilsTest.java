package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionUtilsTest {

    @Test
    void convertToTransaction() {
        // ARRANGE
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(1d);
        transactionDto.setDescription("test");
        User recipient = new User();
        recipient.setId(2L);

        // ACT
        Transaction transaction = TransactionUtils.convertToTransaction(transactionDto, recipient);

        // ASSERT
        assertEquals(transactionDto.getAmount(), transaction.getAmount());
        assertEquals(transactionDto.getDescription(), transaction.getDescription());
        assertEquals(TransactionService.COMMISSION_RATE, transaction.getCommission());
        assertNotNull(transaction.getTimestamp());
        assertEquals(2L, transaction.getRecipient().getId());
        assertEquals(1L, transaction.getPayer().getId());
    }

    @Test
    void convertToTransactionDtoCurrentUserIsPayer() {
        // ARRANGE
        Transaction transaction = new Transaction();
        User payer = new User();
        payer.setId(1L);
        transaction.setPayer(payer);

        User recipient = new User();
        recipient.setId(2L);
        transaction.setRecipient(recipient);

        transaction.setAmount(1d);
        transaction.setDescription("test");

        // ACT
        TransactionDto transactionDto = TransactionUtils.convertToTransactionDto(transaction);

        // ASSERT
        assertEquals(-1 * transaction.getAmount(), transactionDto.getAmount());
        assertEquals(transaction.getDescription(), transactionDto.getDescription());
        assertEquals(transaction.getRecipient().getId(), transactionDto.getContact().getId());
    }

    @Test
    void convertToTransactionDtoCurrentUserIsRecipient() {
        // ARRANGE
        Transaction transaction = new Transaction();
        User payer = new User();
        payer.setId(2L);
        transaction.setPayer(payer);

        User recipient = new User();
        recipient.setId(1L);
        transaction.setRecipient(recipient);

        transaction.setAmount(1d);
        transaction.setDescription("test");

        // ACT
        TransactionDto transactionDto = TransactionUtils.convertToTransactionDto(transaction);

        // ASSERT
        assertEquals(transaction.getAmount(), transactionDto.getAmount());
        assertEquals(transaction.getDescription(), transactionDto.getDescription());
        assertEquals(transaction.getPayer().getId(), transactionDto.getContact().getId());
    }

}
