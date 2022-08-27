package com.openclassrooms.PayMyBuddy.exception;

public class PayerRecipientNotFoundException extends NotFoundException {
    public PayerRecipientNotFoundException() {
        super("PayerRecipient could not be found.");
    }
}
