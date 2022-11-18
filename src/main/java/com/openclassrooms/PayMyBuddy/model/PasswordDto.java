package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordDto {
    @NotBlank(message = "Le mot de passe ne peut pas être vide.")
    private String password;

    @NotBlank(message = "La confirmation de mot de passe ne peut pas être vide.")
    private String passwordConfirmation;
}
