package com.openclassrooms.PayMyBuddy.utils;

import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.PayerRecipient;
import com.openclassrooms.PayMyBuddy.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContactUtilsTest {
    @Test
    void convertToContactDto() {
        // Arrange
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@hgmail.com");
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setRecipientId(1L);

        // Act
        ContactDto contactDto = ContactUtils.convertToContactDto(user, payerRecipient);

        // Assert
        assertEquals(contactDto.getEmail(), user.getEmail());
        assertEquals(contactDto.getFirstName(), user.getFirstName());
        assertEquals(contactDto.getLastName(), user.getLastName());
        assertEquals(contactDto.getRecipientId(), payerRecipient.getRecipientId());
    }
}
