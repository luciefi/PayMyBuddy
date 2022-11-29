package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "L''email ne peut pas être vide.")
    @Pattern(regexp = EmailAddress.EMAIL_ADDRESS_PATTERN, message = EmailAddress.EMAIL_INVALID_MESSAGE)
    private String email;

    private double balance;

    @Column(name = "first_name")
    @NotBlank(message = "Le prénom ne peut pas être vide.")
    private String firstName;

    @NotBlank(message = "Le nom ne peut pas être vide.")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "L''adresse ne peut pas être vide.")
    private String address;

    @NotBlank(message = "Le téléphone ne peut pas être vide.")
    private String phone;
}
