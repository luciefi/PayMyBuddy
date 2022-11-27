package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface ITransactionService {
    Page<TransactionDto> getAllPaginated(int pageNumber);

    @Transactional
    void saveTransaction(TransactionDto transactionDto) throws InsufficientBalanceException;
}
