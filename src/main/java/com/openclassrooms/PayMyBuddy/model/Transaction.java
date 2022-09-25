package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Saisir le montant")
    @Positive(message = "Le montant doit être supérieur à 0")
    @Max(value = 1000000, message = "Le montant doit être inférieur à un million.")
    Double amount;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    Double commission;

    private String description;

    @NotNull
    @Past
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    @NotNull
    private User payer;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @NotNull
    private User recipient;
}
