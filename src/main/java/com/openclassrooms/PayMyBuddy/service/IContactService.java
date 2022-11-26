package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.ContactDto;

public interface IContactService {

    String saveContact(String emailAddress);

    Iterable<ContactDto> getContacts();

    void deleteContact(Long payerRecipientId);

    void updateLastTransactionDate(Long contactId);
}
