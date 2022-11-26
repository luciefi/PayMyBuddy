package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.*;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import com.openclassrooms.PayMyBuddy.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User getUser(final Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserByEmail(final String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public double getBalance() {
        User user = getUser(CurrentUserUtils.getCurrentUserId());
        List<User> users = getUsers();
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<User> sameEmailUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (sameEmailUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        Calendar calendar = Calendar.getInstance();
        user.setDateOfCreation(new Timestamp(calendar.getTime().getTime()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserDto userDto) {
        User existingUser = getUserByEmail(userDto.getEmail());
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
        User user = getUser(CurrentUserUtils.getCurrentUserId());
        encryptPasswords(passwordUpdateDto);
        checkOldPassword(passwordUpdateDto.getOldPassword(), user.getPassword());
        user.setPassword(passwordUpdateDto.getPassword());
        userRepository.save(user);
    }


    @Override
    public void debitBalance(double amountWithCommission) throws InsufficientBalanceException {
        User user = getUser(CurrentUserUtils.getCurrentUserId());
        double balance = user.getBalance();
        if (balance < amountWithCommission) {
            throw new InsufficientBalanceException();
        }
        user.setBalance(balance - amountWithCommission);
        userRepository.save(user);
    }

    @Override
    public void creditBalance(Double amount, Long recipientId) {
        User user = getUser(recipientId);
        Double balance = user.getBalance();
        user.setBalance(balance + amount);
        userRepository.save(user);
    }

    @Override
    public UserDto getCurrentUserDto() {
        User user = getUser(CurrentUserUtils.getCurrentUserId());
        return UserUtils.convertToUserDto(user);
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

    private void encryptPasswords(PasswordUpdateDto passwordUpdateDto) {
        passwordUpdateDto.setOldPassword(passwordEncoder.encode(passwordUpdateDto.getOldPassword()));
        passwordUpdateDto.setPassword(passwordEncoder.encode(passwordUpdateDto.getPassword()));
    }

}
