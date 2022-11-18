package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginDto {
    @NotBlank(message = "L''email ne peut pas être vide.")
    @Pattern(regexp = EmailAddress.EMAIL_ADDRESS_PATTERN, message = EmailAddress.EMAIL_INVALID_MESSAGE)
    private String email;

    @NotBlank(message = "Le mot de passe ne peut pas être vide.")
    private String password;

    private Boolean isRememberMe = false;
}
