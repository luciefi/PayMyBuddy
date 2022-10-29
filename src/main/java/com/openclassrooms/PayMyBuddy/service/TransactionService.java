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

    public static final double COMMISSION_RATE = .005;

    public List<TransactionDto> getAll() {
        List<Transaction> transactions = transactionRepository.findByPayerIdOrRecipientIdOrderByTimestampDesc(CurrentUserUtils.getCurrentUserId(),
                CurrentUserUtils.getCurrentUserId());
        return transactions
                .stream()
                .map(TransactionUtils::convertToTransactionDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveTransaction(TransactionDto transactionDto) throws InsufficientBalanceException {
        User recipient = userRepository.findById(transactionDto.getContactId()).orElseThrow(UserNotFoundException::new);
        Transaction transaction = TransactionUtils.convertToTransaction(transactionDto, recipient);
        double amountWithCommission = transaction.getAmount() * (1 + COMMISSION_RATE);
        userService.debitBalance(amountWithCommission);
        userService.creditBalance(transaction.getAmount(), transactionDto.getContactId());
        transactionRepository.save(transaction);
    }
}
