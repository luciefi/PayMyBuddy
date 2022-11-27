package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IExternalTransactionService {
    void saveExternalTransaction(ExternalTransactionDto externalTransactionDto) throws InsufficientBalanceException;

    Page<ExternalTransaction> getAll(int pageNumber);

    Page<ExternalTransactionDto> getAllDto(int pageNumber);
}

