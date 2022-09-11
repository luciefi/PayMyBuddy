package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;

public interface IExternalTransactionService {
    void saveExternalTransaction(ExternalTransactionDto externalTransactionDto) throws InsufficientBalanceException;

    Iterable<ExternalTransaction> getAll();

    Iterable<ExternalTransactionDto> getAllDto();
}

