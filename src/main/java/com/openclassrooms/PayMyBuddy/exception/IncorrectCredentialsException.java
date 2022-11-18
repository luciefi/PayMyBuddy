package com.openclassrooms.PayMyBuddy.exception;

public class IncorrectCredentialsException extends IllegalArgumentException {
    public IncorrectCredentialsException() {
        super("\u26a0 \u2007 Nom d'utilisateur et/ou mot de passe incorrects");
    }

}
