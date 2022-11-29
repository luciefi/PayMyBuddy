package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import com.openclassrooms.PayMyBuddy.model.TransactionType;

public class ExternalTransactionUtils {
    private ExternalTransactionUtils() {
    }

    public static ExternalTransaction convertToExternalTransaction(ExternalTransactionDto externalTransactionDto, BankAccount bankAccount) {
        ExternalTransaction externalTransaction = new ExternalTransaction();
        externalTransaction.setAmount(externalTransactionDto.getAmount());
        String description = externalTransactionDto.getDescription();
        externalTransaction.setDescription(description.substring(0, Math.min(64, description.length())));
        externalTransaction.setTransactionType(TransactionType.values()[Integer.parseInt(externalTransactionDto.getTransactionType())]);
        externalTransaction.setBankAccount(bankAccount);
        return externalTransaction;
    }

    public static ExternalTransactionDto convertToExternalTransactionDto(ExternalTransaction externalTransaction) {
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        externalTransactionDto.setAmount(externalTransaction.getAmount());
        externalTransactionDto.setDescription(externalTransaction.getDescription());
        externalTransactionDto.setTransactionType(externalTransaction.getTransactionType().getValue() + "");
        externalTransactionDto.setBankAccountId(externalTransaction.getBankAccount().getId());
        externalTransactionDto.setBankAccount(externalTransaction.getBankAccount());
        externalTransactionDto.setTimestamp(externalTransaction.getTimestamp());

        return externalTransactionDto;
    }
}
