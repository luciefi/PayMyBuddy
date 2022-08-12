package com.openclassrooms.PayMyBuddy.exception;

public class BankAccountAlreadyExistsException extends AlreadyExistsException {
    public BankAccountAlreadyExistsException() {
        super("Ce compte existe déjà.");
    }
}
