package com.openclassrooms.PayMyBuddy.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("L'utilisateur n'a pas pu être trouvé");
    }

}
