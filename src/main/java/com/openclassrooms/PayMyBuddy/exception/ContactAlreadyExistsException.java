package com.openclassrooms.PayMyBuddy.exception;

public class ContactAlreadyExistsException extends AlreadyExistsException{
    public ContactAlreadyExistsException()  {
        super("Ce contact existe déjà.");
    }

}
