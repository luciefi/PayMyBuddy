package com.openclassrooms.PayMyBuddy.exception;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String s) {
        super(s);
    }
}