package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.PayMyBuddy.repository.BankAccountRepository;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@Service
public class BankAccountService implements IBankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BankAccount getById(final Long id) {
        return bankAccountRepository.findById(id).orElseThrow(BankAccountNotFoundException::new);
    }

    @Override
    public List<BankAccount> getAllForCurrentUser() {
        return bankAccountRepository.findByUserId(CurrentUserUtils.getCurrentUserId());
    }

    @Override
    public void deleteBankAccount(final Long id) {
        bankAccountRepository.deleteById(id);
    }

    @Override
    public BankAccount saveBankAccount(BankAccount bankAccount) {
        if (isBankAccountInvalid(bankAccount)) {
            throw new BankAccountAlreadyExistsException();
        }
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(RuntimeException::new);
        bankAccount.setUser(user);
        Calendar calendar = Calendar.getInstance();
        bankAccount.setDateOfCreation(new Timestamp(calendar.getTime().getTime()));
        return bankAccountRepository.save(bankAccount);
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
