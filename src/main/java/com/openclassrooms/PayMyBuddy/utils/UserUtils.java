package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.ProfileDto;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.model.UserDto;

public class UserUtils {
    private UserUtils(){}
    public static User convertToUser(ProfileDto profileDto) {
        User user = convertToUser(profileDto.getUserDto());
        user.setPassword(profileDto.getPasswordDto().getPassword());
        return user;
    }

    public static User convertToUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setId(userDto.getId());
        return user;
    }

    public static UserDto convertToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setAddress(user.getAddress());
        return userDto;
    }
}
