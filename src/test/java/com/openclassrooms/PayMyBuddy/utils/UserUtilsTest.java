package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.ProfileDto;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserUtilsTest {

    @Test
    void convertToUserDtoTest() {
        // Arrange
        User user = new User();
        user.setFirstName("john");
        user.setLastName("doe");
        user.setEmail("jd@abc.com");
        user.setPhone("phone");
        user.setAddress("address");

        // Act
        UserDto userDto = UserUtils.convertToUserDto(user);

        // Assert
        assertEquals("john", userDto.getFirstName());
        assertEquals("doe", userDto.getLastName());
        assertEquals("jd@abc.com", userDto.getEmail());
        assertEquals("phone", userDto.getPhone());
        assertEquals("address", userDto.getAddress());
    }

    @Test
    void convertToUserTest() {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setFirstName("john");
        userDto.setLastName("doe");
        userDto.setEmail("jd@abc.com");
        userDto.setPhone("phone");
        userDto.setAddress("address");
        userDto.setId(1L);

        // Act
        User user = UserUtils.convertToUser(userDto);

        // Assert
        assertEquals(1L, user.getId());
        assertEquals("john", user.getFirstName());
        assertEquals("doe", user.getLastName());
        assertEquals("jd@abc.com", user.getEmail());
        assertEquals(null, user.getPassword());
        assertEquals("phone", user.getPhone());
        assertEquals("address", user.getAddress());
    }
}
