package com.openclassrooms.PayMyBuddy.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("\u26a0 \u2007 Le solde est insuffisant.");
    }
}
