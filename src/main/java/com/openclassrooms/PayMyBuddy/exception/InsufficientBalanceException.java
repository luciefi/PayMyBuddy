package com.openclassrooms.PayMyBuddy.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(){
        super("Le solde est insuffisant.");
    }
}
