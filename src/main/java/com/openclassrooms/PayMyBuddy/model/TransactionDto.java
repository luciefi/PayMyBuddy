package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Data
public class TransactionDto {

    @NotNull(message = "Saisir le montant")
    @Positive(message = "Le montant doit être supérieur à 0.")
    @Max(value = 1000000, message = "Le montant doit être inférieur à un million.")
    private Double amount;

    private String description;

    @Positive(message = "Choisir le contact")
    private Long contactId;

    private Timestamp timestamp;

    private User contact;

}
