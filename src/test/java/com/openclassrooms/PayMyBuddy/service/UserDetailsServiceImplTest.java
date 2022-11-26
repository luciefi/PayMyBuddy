package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDetailsImpl;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration
class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void loadUserByUsername() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("user@gmail.com");

        // Assert
        assertTrue(userDetails instanceof UserDetailsImpl);
        assertEquals(user.getId(), ((UserDetailsImpl) userDetails).getId());
        verify(userRepository, times(1)).findByEmailIgnoreCase(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void loadUserByUsernameNotFound() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername("user@gmail.com"));

        // Assert
        verify(userRepository, times(1)).findByEmailIgnoreCase(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}