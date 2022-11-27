package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.model.ContactDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {

    String saveContact(String emailAddress);

    Page<ContactDto> getPaginatedContacts(int pageNumber);

    List<ContactDto> getContacts();

    void deleteContact(Long payerRecipientId);

    void updateLastTransactionDate(Long contactId);
}
