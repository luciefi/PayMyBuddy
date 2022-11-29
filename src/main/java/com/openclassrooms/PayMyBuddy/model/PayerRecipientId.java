package com.openclassrooms.PayMyBuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayerRecipientId implements Serializable {
    private Long payerId;
    private Long recipientId;
}
