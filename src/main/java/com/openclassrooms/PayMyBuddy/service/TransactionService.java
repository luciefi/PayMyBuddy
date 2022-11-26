package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import com.openclassrooms.PayMyBuddy.utils.TransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    public static final double COMMISSION_RATE = .005;

    @Override
    public List<TransactionDto> getAll() {
        List<Transaction> transactions = transactionRepository.findByPayerIdOrRecipientIdOrderByTimestampDesc(CurrentUserUtils.getCurrentUserId(),
                CurrentUserUtils.getCurrentUserId());
        return transactions
                .stream()
                .map(TransactionUtils::convertToTransactionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveTransaction(TransactionDto transactionDto) throws InsufficientBalanceException {
        User recipient = userService.getUser(transactionDto.getContactId());
        Transaction transaction = TransactionUtils.convertToTransaction(transactionDto, recipient);
        updateBalancesAndLastTransactionDate(transaction.getAmount(), transactionDto.getContactId());
        transactionRepository.save(transaction);
    }

    public void updateBalancesAndLastTransactionDate(Double transactionAmount, Long contactId) throws InsufficientBalanceException {
        double amountWithCommission = transactionAmount * (1 + TransactionService.COMMISSION_RATE);
        userService.debitBalance(amountWithCommission);
        userService.creditBalance(transactionAmount, contactId);
        contactService.updateLastTransactionDate(contactId);
    }
}