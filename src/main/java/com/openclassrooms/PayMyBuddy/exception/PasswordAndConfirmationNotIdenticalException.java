package com.openclassrooms.PayMyBuddy.exception;

public class PasswordAndConfirmationNotIdenticalException extends IllegalArgumentException {
    public PasswordAndConfirmationNotIdenticalException() {
        super("\u26a0 \u2007 Le mot de passe et la confirmation sont différents.");
    }
}
