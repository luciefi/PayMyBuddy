package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.PayerRecipientId;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.TransactionDto;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.TransactionService;

import java.sql.Timestamp;
import java.util.Calendar;

public class TransactionUtils {
    public static TransactionDto convertToTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        if (transaction.getPayer().getId().equals(CurrentUserUtils.getCurrentUserId())) {
            transactionDto.setContact(transaction.getRecipient());
            transactionDto.setAmount(-1 * transaction.getAmount());
        } else {
            transactionDto.setContact(transaction.getPayer());
            transactionDto.setAmount(transaction.getAmount());
        }
        transactionDto.setDescription(transaction.getDescription());
        return transactionDto;
    }

    public static Transaction convertToTransaction(TransactionDto transactionDto, User recipient) {
        Transaction transaction = new Transaction();
        transaction.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
        User payer = new User();
        payer.setId(CurrentUserUtils.getCurrentUserId());
        transaction.setPayer(payer); // TODO tester que c'est ok, éviter un appel à la base
        transaction.setRecipient(recipient);
        transaction.setAmount(transactionDto.getAmount());
        transaction.setCommission(TransactionService.COMMISSION_RATE);
        transaction.setDescription(transactionDto.getDescription());
        return transaction;
    }
}
