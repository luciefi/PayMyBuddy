package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.*;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getBalance() {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT - ASSERT
        assertEquals(10f, userService.getBalance());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void getBalanceUnknownUser() {
        // ARRANGE
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT - ASSERT
        assertThrows(UserNotFoundException.class, () -> userService.getBalance());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateBalanceCredit() throws InsufficientBalanceException {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT
        userService.updateBalance(5d, TransactionType.DEBIT_EXTERNAL_ACCOUNT);

        // ASSERT
        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(15f, user.getBalance());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void updateBalanceDebit() throws InsufficientBalanceException {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT
        userService.updateBalance(5d, TransactionType.CREDIT_EXTERNAL_ACCOUNT);

        // ASSERT
        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(5f, user.getBalance());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateBalanceInsufficientBalance() throws InsufficientBalanceException {
        // ARRANGE
        User user = new User();
        user.setBalance(10f);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // ACT
        assertThrows(InsufficientBalanceException.class, () -> userService.updateBalance(50d, TransactionType.CREDIT_EXTERNAL_ACCOUNT));

        // ASSERT
        verify(userRepository, times(1)).findById(anyLong());
        assertEquals(10f, user.getBalance());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updateBalanceUnknownUser() {
        // ARRANGE
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // ACT - ASSERT
        assertThrows(UserNotFoundException.class, () -> userService.updateBalance(1d, TransactionType.CREDIT_EXTERNAL_ACCOUNT));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void debitBalance() throws InsufficientBalanceException {
        // Arrange
        User user = new User();
        user.setBalance(10000d);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        userService.debitBalance(1d);

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void debitBalanceUserNotFound() throws InsufficientBalanceException {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> userService.debitBalance(1d));

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void debitBalanceInsufficientBalanceException() {
        // Arrange
        User user = new User();
        user.setBalance(0d);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        assertThrows(InsufficientBalanceException.class, () -> userService.debitBalance(1d));

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void creditBalance() {
        // Arrange
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        userService.creditBalance(1d, 1L);

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void creditBalanceUserNotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> userService.creditBalance(1d, 1L));

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveNewUser() {
        // Arrange
        ProfileDto profileDto = new ProfileDto();
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("password");
        passwordDto.setPasswordConfirmation("password");
        profileDto.setPasswordDto(passwordDto);
        UserDto userDto = new UserDto();
        userDto.setEmail("abc@de.com");
        profileDto.setUserDto(userDto);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        userService.saveNewUser(profileDto);

        // Assert
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void saveNewUserPasswordAndConfirmationNotIdenticalException() {
        // Arrange
        ProfileDto profileDto = new ProfileDto();
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("password");
        passwordDto.setPasswordConfirmation("different");
        profileDto.setPasswordDto(passwordDto);

        // Act
        assertThrows(PasswordAndConfirmationNotIdenticalException.class, () -> userService.saveNewUser(profileDto));

        // Assert
        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void saveNewUserEmailAlreadyExistsException() {
        // Arrange
        ProfileDto profileDto = new ProfileDto();
        PasswordDto passwordDto = new PasswordDto();
        passwordDto.setPassword("password");
        passwordDto.setPasswordConfirmation("password");
        profileDto.setPasswordDto(passwordDto);
        UserDto userDto = new UserDto();
        userDto.setEmail("abc@de.com");
        profileDto.setUserDto(userDto);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // Act
        assertThrows(EmailAlreadyExistsException.class, () -> userService.saveNewUser(profileDto));

        // Assert
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("abc@de.com");
        userDto.setId(1L);
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        userService.updateUser(userDto);

        // Assert
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUserNotFound() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("abc@de.com");
        userDto.setId(1L);
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(userDto));

        // Assert
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserEmailAlreadyExists() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setEmail("abc@de.com");
        userDto.setId(1L);
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // Act
        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(userDto));

        // Assert
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatePassword(){
        // Arrange
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto();
        passwordUpdateDto.setOldPassword("password");
        passwordUpdateDto.setPassword("password");
        passwordUpdateDto.setPasswordConfirmation("password");
        User user = new User();
        user.setPassword("password");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        userService.updatePassword(passwordUpdateDto);

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updatePasswordCurrentPasswordIncorrect(){
        // Arrange
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto();
        passwordUpdateDto.setOldPassword("password");
        passwordUpdateDto.setPassword("password");
        passwordUpdateDto.setPasswordConfirmation("password");
        User user = new User();
        user.setPassword("different_password");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // Act
        assertThrows(IncorrectCurrentPasswordException.class, () -> userService.updatePassword(passwordUpdateDto));

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatePasswordConfirmationDifferent(){
        // Arrange
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto();
        passwordUpdateDto.setOldPassword("password");
        passwordUpdateDto.setPassword("password");
        passwordUpdateDto.setPasswordConfirmation("different_password");

        // Act
        assertThrows(PasswordAndConfirmationNotIdenticalException.class, () -> userService.updatePassword(passwordUpdateDto));

        // Assert
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updatePasswordUserNotFound(){
        // Arrange
        PasswordUpdateDto passwordUpdateDto = new PasswordUpdateDto();
        passwordUpdateDto.setOldPassword("password");
        passwordUpdateDto.setPassword("password");
        passwordUpdateDto.setPasswordConfirmation("password");
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(passwordUpdateDto));

        // Assert
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }
}