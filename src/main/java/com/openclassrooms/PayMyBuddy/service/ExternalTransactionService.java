package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;
import com.openclassrooms.PayMyBuddy.repository.ExternalTransactionRepository;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import com.openclassrooms.PayMyBuddy.utils.ExternalTransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalTransactionService implements IExternalTransactionService {

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
    public List<ExternalTransaction> getAll() {
        return externalTransactionRepository.findByUserId(CurrentUserUtils.getCurrentUserId());
    }

    @Override
    public List<ExternalTransactionDto> getAllDto() {
        List<ExternalTransaction> externalTransactionList = getAll();
        List<ExternalTransactionDto> externalTransactionDtos = externalTransactionList.stream().map(externalTransaction -> ExternalTransactionUtils.convertToExternalTransactionDto(externalTransaction)).collect(Collectors.toList());
        return externalTransactionDtos;
    }
}
