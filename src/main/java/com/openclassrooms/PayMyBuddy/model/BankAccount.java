package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "L''IBAN ne peut pas être vide.")
    @Size(min = 12, max = 34, message = "L''IBAN doit contenir entre 12 et 34 caractères.")
    private String iban;

    @NotBlank(message = "Le BIC ne peut pas être vide.")
    @Size(min = 8, max = 11, message = "Le BIC doit contenir entre 8 et 11 caractères.")
    private String bic;

    @NotBlank(message = "Le nom ne peut pas être vide.")
    private String name;

    @Column(name = "date_of_creation")
    private Timestamp dateOfCreation;

    @Column(name = "last_transaction_date")
    private Timestamp lastTransactionDate;

    private boolean deactivated;
}
