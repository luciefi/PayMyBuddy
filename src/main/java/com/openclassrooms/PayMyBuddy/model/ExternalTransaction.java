package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Timestamp;

@Data
public class ExternalTransaction {
    @Id
    @NotNull
    Long id;

    @NotNull
    Float amount;

    private String description;

    @NotNull
    @Past
    private Timestamp timestamp;

    @NotNull
    private TransactionType transactionType;

    @NotNull
    private Long bankAccountId;

    @NotNull
    private Long userId;
}
