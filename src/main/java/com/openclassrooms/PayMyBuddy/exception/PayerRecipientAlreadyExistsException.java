package com.openclassrooms.PayMyBuddy.exception;

public class PayerRecipientAlreadyExistsException extends AlreadyExistsException {
    public PayerRecipientAlreadyExistsException() {
        super("\u26a0 \u2007 Ce contact existe déjà.");
    }
}
