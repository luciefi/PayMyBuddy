package com.openclassrooms.PayMyBuddy.exception;

public class BankAccountAlreadyExistsException extends AlreadyExistsException {
    public BankAccountAlreadyExistsException() {
        super("\u26a0 Ce compte existe déjà.");
    }
}
