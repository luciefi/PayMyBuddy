package com.openclassrooms.PayMyBuddy.exception;

public class BankAccountNotFoundException extends NotFoundException {
    public BankAccountNotFoundException() {
        super("Bank account could not be found.");
    }
}