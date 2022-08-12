package com.openclassrooms.PayMyBuddy.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayerRecipientId  implements Serializable {
    private User payer;
    private User recipient;
}
