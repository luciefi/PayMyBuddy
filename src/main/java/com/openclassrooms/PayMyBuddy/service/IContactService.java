package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.EmailAddress;

public interface IContactService {

    String saveContact(String emailAddress);

    Iterable<ContactDto> getContacts();

    void deleteContact(String email);
}
