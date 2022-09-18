package com.openclassrooms.PayMyBuddy.exception;

public class BankAccountNotFoundException extends NotFoundException {
    public BankAccountNotFoundException() {
        super("Compte bancaire introuvable.");
    }
}