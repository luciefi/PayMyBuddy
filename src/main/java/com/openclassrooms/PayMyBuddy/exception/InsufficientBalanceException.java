package com.openclassrooms.PayMyBuddy.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(){
        super("\u26a0 Le solde est insuffisant.");
    }
}
