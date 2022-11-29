package com.openclassrooms.PayMyBuddy.exception;

public class ContactCannotBeCurrentUserException extends IllegalArgumentException {
    public ContactCannotBeCurrentUserException() {
        super("L'email doit être différent du vôtre");
    }
}
