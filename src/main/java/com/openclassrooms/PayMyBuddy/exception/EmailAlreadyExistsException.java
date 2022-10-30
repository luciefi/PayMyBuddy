package com.openclassrooms.PayMyBuddy.exception;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException() {
        super("\u26a0 Cette adresse mail est déjà utilisée.");
    }
}
