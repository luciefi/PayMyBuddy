package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
public class ContactDto {
    private String firstName;

    private String lastName;

    @NotBlank(message = "L''email ne peut pas être vide.")
    @Pattern(regexp = EmailAddress.EMAIL_ADDRESS_PATTERN, message = EmailAddress.EMAIL_INVALID_MESSAGE)
    private String email;

    private Timestamp dateOfCreation;

    private Long recipientId;
}
