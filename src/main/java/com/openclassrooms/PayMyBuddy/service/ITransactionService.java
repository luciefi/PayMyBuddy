package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.TransactionDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITransactionService {
    List<TransactionDto> getAll();

    @Transactional
    void saveTransaction(TransactionDto transactionDto) throws InsufficientBalanceException;
}
