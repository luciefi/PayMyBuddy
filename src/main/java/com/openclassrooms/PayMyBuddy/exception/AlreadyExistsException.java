package com.openclassrooms.PayMyBuddy.exception;

public class AlreadyExistsException extends IllegalArgumentException {
    public AlreadyExistsException(String s) {
        super(s);
    }
}
