package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.*;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import com.openclassrooms.PayMyBuddy.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUser(final Long id) {
        return userRepository.findById(id);
    } // TODO vérifier si utile

    @Override
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    } // TODO devrait être une liste

    @Override
    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public double getBalance() {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        return user.getBalance();
    }

    @Override
    public void updateBalance(Double amount, TransactionType type) throws InsufficientBalanceException {
        if (type.equals(TransactionType.CREDIT_EXTERNAL_ACCOUNT)) {
            debitBalance(amount);
        } else {
            creditBalance(amount, CurrentUserUtils.getCurrentUserId());
        }
    }

    @Override
    public User saveNewUser(ProfileDto profileDto) {
        checkPasswordConfirmation(profileDto.getPasswordDto());
        User user = UserUtils.convertToUser(profileDto);
        Optional<User> sameEmailUser = userRepository.findByEmail(user.getEmail());
        if (sameEmailUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        Calendar calendar = Calendar.getInstance();
        user.setDateOfCreation(new Timestamp(calendar.getTime().getTime()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail()).orElseThrow(UserNotFoundException::new);
        User user = UserUtils.convertToUser(userDto);
        if (!CurrentUserUtils.getCurrentUserId().equals(existingUser.getId())) {
            throw new EmailAlreadyExistsException();
        }
        user.setDateOfCreation(existingUser.getDateOfCreation());
        user.setLastOnlineTime(existingUser.getLastOnlineTime());
        user.setPassword(existingUser.getPassword());
        return userRepository.save(user);
    }

    @Override
    public void updatePassword(PasswordUpdateDto passwordUpdateDto) {
        checkPasswordConfirmation(passwordUpdateDto);
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        checkOldPassword(passwordUpdateDto.getOldPassword(), user.getPassword());
        user.setPassword(passwordUpdateDto.getPassword());
        userRepository.save(user);
    }

    @Override
    public void debitBalance(double amountWithCommission) throws InsufficientBalanceException {
        User user = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        double balance = user.getBalance();
        if (balance < amountWithCommission) {
            throw new InsufficientBalanceException();
        }
        user.setBalance(balance - amountWithCommission);
        userRepository.save(user);
    }

    @Override
    public void creditBalance(Double amount, Long recipientId) {
        User user = userRepository.findById(recipientId).orElseThrow(UserNotFoundException::new);
        Double balance = user.getBalance();
        user.setBalance(balance + amount);
        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUserDto() {
        User user = getUser(CurrentUserUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        return UserUtils.convertToUserDto(user);
    }

    @Override
    public void logUserIn(LoginDto loginDto) throws IncorrectCredentialsException {
        User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(UserNotFoundException::new);
        if (user.getPassword() != loginDto.getPassword()) {
            throw new IncorrectCredentialsException();
        }
        Calendar calendar = Calendar.getInstance();
        user.setLastOnlineTime(new Timestamp(calendar.getTime().getTime()));
        userRepository.save(user);
        // TODO current user à updater
    }

    private void checkPasswordConfirmation(PasswordDto passwordDto) {
        if (!passwordDto.getPassword().equals(passwordDto.getPasswordConfirmation())) {
            throw new PasswordAndConfirmationNotIdenticalException();
        }
    }

    private void checkOldPassword(String passwordFromInput, String passwordFromBase) {
        if (!passwordFromInput.equals(passwordFromBase)) {
            throw new IncorrectCurrentPasswordException();
        }
    }
}
