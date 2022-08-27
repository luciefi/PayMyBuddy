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

    private boolean deleted;

    @Id
    @Column(name = "payer_id")
    private Long payerId;

    @ManyToOne
    @JoinColumn(name = "payer_id", insertable = false, updatable = false)
    private User payer;

    @Id
    @Column(name = "recipient_id")
    private Long recipientId;

    @ManyToOne
    @JoinColumn(name = "recipient_id", insertable = false, updatable = false)
    private User recipient;
}
