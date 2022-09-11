package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import com.openclassrooms.PayMyBuddy.model.TransactionType;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ExternalTransactionUtils {
    public static ExternalTransaction convertToExternalTransaction(ExternalTransactionDto externalTransactionDto, BankAccount bankAccount) {
        ExternalTransaction externalTransaction = new ExternalTransaction();
        externalTransaction.setAmount(Float.valueOf(externalTransactionDto.getAmount()));
        String description = externalTransactionDto.getDescription();
        externalTransaction.setDescription(description.substring(0, Math.min(64, description.length())));
        externalTransaction.setTransactionType(TransactionType.values()[Integer.parseInt(externalTransactionDto.getTransactionType())]);
        externalTransaction.setBankAccount(bankAccount);
        return externalTransaction;
    }

    public static ExternalTransactionDto convertToExternalTransactionDto(ExternalTransaction externalTransaction) {
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        String sign = externalTransaction.getTransactionType().equals(TransactionType.CREDIT_EXTERNAL_ACCOUNT) ? "-" : "+";
        externalTransactionDto.setAmount(sign + " " + externalTransaction.getAmount());
        externalTransactionDto.setDescription(externalTransaction.getDescription());
        externalTransactionDto.setTransactionType(externalTransaction.getTransactionType().getValue() + "");
        externalTransactionDto.setBankAccountId(externalTransaction.getBankAccount().getId().toString());
        externalTransactionDto.setBankAccount(externalTransaction.getBankAccount());
        externalTransactionDto.setName(externalTransaction.getBankAccount().getName());
        externalTransactionDto.setTimestamp(externalTransaction.getTimestamp());

        return externalTransactionDto;
    }
}
