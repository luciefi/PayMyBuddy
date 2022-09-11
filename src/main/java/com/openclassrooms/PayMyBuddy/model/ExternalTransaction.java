package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "external_transaction")
public class ExternalTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Saisir le montant")
    @Positive(message="Le montant doit être supérieur à 0")
    @Digits(message = "Le montant doit être un nombre", integer = 8, fraction = 2)
    Float amount;

    private String description;

    @NotNull
    @Past
    private Timestamp timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    @NotNull
    private Long userId;
}
