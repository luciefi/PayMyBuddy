package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.sql.Timestamp;

@Data
public class ExternalTransactionDto {
    /*
    @Pattern(regexp = "0*[0-9]{1,8}(,[0-9]([0-9])?)?", message = "Le montant doit être un nombre au format XXX,XX ")
    @Size(max=11, message = "Le montant doit être inférieur à un million")
    @Pattern(regexp = "0*[1-9]+[0-9]{0,7}(,[0-9]([0-9])?)?|0*[0-9]{1,8},[1-9]([0-9])?|0*[0-9]{1,8},[0-9]([1-9])?", message = "Le montant doit " +
            "être supérieur à 0")
            */
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
