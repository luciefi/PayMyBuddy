package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.sql.Timestamp;

@Data
public class ExternalTransactionDto {

    @NotNull(message = "Saisir le montant")
    @Positive(message = "Le montant doit être supérieur à 0.")
    @Max(value = 1000000, message = "Le montant doit être inférieur à un million.")
    Double amount;

    private String description;

    @NotNull(message = "Choisir le type de transaction")
    private String transactionType;

    @Positive(message = "Choisir le compte bancaire")
    private Long bankAccountId;

    private Timestamp timestamp;

    private BankAccount bankAccount;

}
