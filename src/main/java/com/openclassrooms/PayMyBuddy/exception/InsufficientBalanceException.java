package com.openclassrooms.PayMyBuddy.exception;

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException(){
        super("Virement impossible : le solde est insuffisant");
    }
}
