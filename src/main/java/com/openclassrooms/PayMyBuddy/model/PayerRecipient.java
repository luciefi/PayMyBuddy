package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "payer_recipient")
@IdClass(PayerRecipientId.class)
public class PayerRecipient {

    @Column(name = "date_of_creation")
    private Timestamp dateOfCreation;

    @Column(name = "last_transaction_date")
    private Timestamp lastTransactionDate;

    @Id
    @ManyToOne                          // ou manyToMany ?
    @JoinColumn(name = "payer_id")
    private User payer;

    @Id
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;
}
