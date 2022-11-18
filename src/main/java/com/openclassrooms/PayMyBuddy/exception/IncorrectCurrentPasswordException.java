package com.openclassrooms.PayMyBuddy.exception;

public class IncorrectCurrentPasswordException extends IllegalArgumentException {
    public IncorrectCurrentPasswordException() {
        super("\u26a0 \u2007 Le mot de passe actuel est incorrect.");
    }
}
