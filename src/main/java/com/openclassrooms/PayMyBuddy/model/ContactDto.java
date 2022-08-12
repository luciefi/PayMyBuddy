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
    @Pattern(regexp = "[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", message = "L''adresse email n''est pas valide")
    private String email;

    private Timestamp dateOfCreation;
}
