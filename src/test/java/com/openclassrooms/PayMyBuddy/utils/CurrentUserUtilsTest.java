package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.configuration.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class CurrentUserUtilsTest {

    @Test
    @WithAnonymousUser
    void getCurrentUserIdUnauthenticated() {
        // Act
        Long currentUserId = CurrentUserUtils.getCurrentUserId();

        // Assert
        assertNull(currentUserId);
    }

    @Test
    @WithMockCustomUser
    void getCurrentUserIdAuthenticated() {
        // Act
        Long currentUserId = CurrentUserUtils.getCurrentUserId();

        // Assert
        assertEquals(currentUserId, 1L);
    }
}
