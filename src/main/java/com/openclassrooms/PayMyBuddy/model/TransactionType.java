package com.openclassrooms.PayMyBuddy.model;

public enum TransactionType {
    CREDIT_EXTERNAL_ACCOUNT(0), DEBIT_EXTERNAL_ACCOUNT(1);

    private int transactionType;

    TransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public int getValue() {
        return this.transactionType;
    }
}
