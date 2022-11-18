package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordUpdateDto extends PasswordDto {
    @NotBlank(message = "L''ancien mot de passe ne peut pas être vide.")
    private String oldPassword;
}