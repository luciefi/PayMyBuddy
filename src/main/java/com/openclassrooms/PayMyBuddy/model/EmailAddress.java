package com.openclassrooms.PayMyBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAddress {
    static final String EMAIL_ADDRESS_PATTERN = "[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    static final String EMAIL_INVALID_MESSAGE = "L''adresse email n''est pas valide";

    @NotBlank(message = "L''email ne peut pas être vide.")
    @Pattern(regexp = EMAIL_ADDRESS_PATTERN, message = EMAIL_INVALID_MESSAGE)
    private String address;

}

