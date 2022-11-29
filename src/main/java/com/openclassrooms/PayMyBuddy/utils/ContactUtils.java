package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.PayerRecipient;
import com.openclassrooms.PayMyBuddy.model.User;

public class ContactUtils {
    private ContactUtils() {
    }

    public static ContactDto convertToContactDto(User user, PayerRecipient payerRecipient) {
        ContactDto contactDto = new ContactDto();
        contactDto.setEmail(user.getEmail());
        contactDto.setFirstName(user.getFirstName());
        contactDto.setLastName(user.getLastName());
        contactDto.setRecipientId(payerRecipient.getRecipientId());
        return contactDto;
    }
}