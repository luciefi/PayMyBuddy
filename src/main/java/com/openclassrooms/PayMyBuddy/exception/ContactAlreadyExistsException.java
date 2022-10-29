package com.openclassrooms.PayMyBuddy.exception;

public class ContactAlreadyExistsException extends AlreadyExistsException{
    public ContactAlreadyExistsException()  {
        super("\u26a0 Ce contact existe déjà.");
    }

}
