package com.openclassrooms.PayMyBuddy.exception;

public class PayerRecipientAlreadyExistsException extends AlreadyExistsException {
    public PayerRecipientAlreadyExistsException() {
        super("Ce contact existe déjà.");
    }
}
