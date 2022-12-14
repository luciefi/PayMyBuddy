package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class BankAccountService implements IBankAccountService {
    private static final int BANK_ACCOUNT_PAGE_SIZE = 5;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BankAccount getById(final Long id) {
        return bankAccountRepository.findById(id).orElseThrow(BankAccountNotFoundException::new);
    }

    @Override
    public Page<BankAccount> getPaginatedForCurrentUser(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, BANK_ACCOUNT_PAGE_SIZE);
        return bankAccountRepository.findByUserId(CurrentUserUtils.getCurrentUserId(), pageable);
    }

    @Override
    public void deleteBankAccount(final Long id) {
        BankAccount bankAccount = bankAccountRepository.findByIdAndUserId(id, CurrentUserUtils.getCurrentUserId()).orElseThrow(BankAccountNotFoundException::new);
        bankAccount.setDeactivated(true);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount saveBankAccount(BankAccount bankAccount) {
        if (isBankAccountInvalid(bankAccount)) {
            throw new BankAccountAlreadyExistsException();
        }
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(RuntimeException::new);
        bankAccount.setUser(user);
        bankAccount.setDeactivated(false);
        Calendar calendar = Calendar.getInstance();
        bankAccount.setDateOfCreation(new Timestamp(calendar.getTime().getTime()));
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public Iterable<BankAccount> getAllActiveForCurrentUser() {
        return bankAccountRepository.findByUserIdAndDeactivated(CurrentUserUtils.getCurrentUserId(), false);
    }

    @Override
    public void activateBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findByIdAndUserId(id, CurrentUserUtils.getCurrentUserId()).orElseThrow(BankAccountNotFoundException::new);
        bankAccount.setDeactivated(false);
        bankAccountRepository.save(bankAccount);
    }

    private boolean isBankAccountInvalid(BankAccount bankAccount) {
        List<BankAccount> accountList;
        if (bankAccount.getId() == null) {
            accountList = bankAccountRepository.findByIbanAndBicAndUserId(
                    bankAccount.getIban(),
                    bankAccount.getBic(),
                    CurrentUserUtils.getCurrentUserId()
            );
        } else {
            accountList = bankAccountRepository.findByIbanAndBicAndUserIdAndIdNot(
                    bankAccount.getIban(),
                    bankAccount.getBic(),
                    CurrentUserUtils.getCurrentUserId(),
                    bankAccount.getId()
            );
        }
        return accountList.size() > 0;
    }
}
