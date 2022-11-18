package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.Valid;

@Data
public class ProfileDto {

    @Valid
    private UserDto userDto;

    @Valid
    private PasswordDto passwordDto;

    public ProfileDto() {
        this.userDto = new UserDto();
        this.passwordDto = new PasswordDto();
    }

    public ProfileDto(UserDto userDto) {
        this.userDto = userDto;
        this.passwordDto = new PasswordUpdateDto();
    }
}
