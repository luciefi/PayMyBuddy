package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;
import com.openclassrooms.PayMyBuddy.repository.ExternalTransactionRepository;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import com.openclassrooms.PayMyBuddy.utils.ExternalTransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalTransactionService implements IExternalTransactionService {

    private static final int EXTERNAL_TRANSACTION_PAGE_SIZE = 5;
    @Autowired
    private ExternalTransactionRepository externalTransactionRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BankAccountService bankAccountService;

    @Override
    public void saveExternalTransaction(ExternalTransactionDto externalTransactionDto) throws InsufficientBalanceException {
        ExternalTransaction externalTransaction = createExternalTransaction(externalTransactionDto);
        userService.updateBalance(externalTransaction.getAmount(), externalTransaction.getTransactionType());
        Calendar calendar = Calendar.getInstance();
        externalTransaction.getBankAccount().setLastTransactionDate(new Timestamp(calendar.getTime().getTime()));
        externalTransaction.setTimestamp(new Timestamp(Calendar.getInstance().getTime().getTime()));
        externalTransactionRepository.save(externalTransaction);
    }

    private ExternalTransaction createExternalTransaction(ExternalTransactionDto externalTransactionDto) {
        BankAccount bankAccount = bankAccountRepository.findById(externalTransactionDto.getBankAccountId()).orElseThrow(BankAccountNotFoundException::new);
        ExternalTransaction externalTransaction = ExternalTransactionUtils.convertToExternalTransaction(externalTransactionDto, bankAccount);
        externalTransaction.setUserId(CurrentUserUtils.getCurrentUserId());
        return externalTransaction;
    }

    @Override
    public Page<ExternalTransaction> getAll(int pageNumber) {
        int startItem = pageNumber * EXTERNAL_TRANSACTION_PAGE_SIZE;
        Pageable pageable = PageRequest.of(pageNumber, EXTERNAL_TRANSACTION_PAGE_SIZE);
        return externalTransactionRepository.findByUserId(CurrentUserUtils.getCurrentUserId(), pageable);
    }

    @Override
    public Page<ExternalTransactionDto> getAllDto(int pageNumber) {
        Page<ExternalTransaction> externalTransactionList = getAll(pageNumber);
        Page<ExternalTransactionDto> externalTransactionDtos =
                externalTransactionList.map(ExternalTransactionUtils::convertToExternalTransactionDto);
        return externalTransactionDtos;
    }
}
