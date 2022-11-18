package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

@Data
public class ProfileUpdateDto {
    private UserDto userDto;
    private PasswordUpdateDto passwordUpdateDto;

    public ProfileUpdateDto(UserDto userDto) {
        this.userDto = userDto;
        this.passwordUpdateDto = new PasswordUpdateDto();
    }
}