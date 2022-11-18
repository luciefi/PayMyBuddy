package com.openclassrooms.PayMyBuddy.exception;

public class ContactAlreadyExistsException extends AlreadyExistsException{
    public ContactAlreadyExistsException()  {
        super("\u26a0 \u2007 Ce contact existe déjà.");
    }

}
