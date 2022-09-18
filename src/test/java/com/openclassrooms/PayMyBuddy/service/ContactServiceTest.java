package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.ContactCannotBeCurrentUserException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.PayerRecipient;
import com.openclassrooms.PayMyBuddy.model.PayerRecipientId;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.PayerRecipientRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private PayerRecipientRepository payerRecipientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ContactService contactService;

    final Long PAYER_ID = Long.valueOf(1);
    final Long RECIPIENT_ID = Long.valueOf(2);

    final String PAYER_EMAIL = "payer@abc.com";
    final String RECIPIENT_EMAIL = "recipient@abc.com";

    @Test
    void getContacts() {
        // Arrange
        PayerRecipient payerRecipient = new PayerRecipient();
        User recipient = new User();
        recipient.setId(RECIPIENT_ID);
        recipient.setEmail(RECIPIENT_EMAIL);
        payerRecipient.setRecipient(recipient);
        when(payerRecipientRepository.findByPayerIdAndDeleted(anyLong(), anyBoolean())).thenReturn(Collections.singletonList(payerRecipient));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(recipient));

        // Act
        Iterable<ContactDto> contacts = (List<ContactDto>) contactService.getContacts();

        // Assert
        assertThat(contacts).isInstanceOf(List.class);
        assertThat(((List<ContactDto>) contacts).size()).isEqualTo(1);
        assertThat(((List<ContactDto>) contacts).get(0).getEmail()).isEqualTo(RECIPIENT_EMAIL);
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndDeleted(anyLong(), anyBoolean());
        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void getUnknownContacts() {
        // Arrange
        PayerRecipient payerRecipient = new PayerRecipient();
        User recipient = new User();
        recipient.setId(RECIPIENT_ID);
        recipient.setEmail(RECIPIENT_EMAIL);
        payerRecipient.setRecipient(recipient);
        when(payerRecipientRepository.findByPayerIdAndDeleted(anyLong(), anyBoolean())).thenReturn(Collections.singletonList(payerRecipient));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> contactService.getContacts());

        // Assert
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndDeleted(anyLong(), anyBoolean());
        verify(userRepository, Mockito.times(1)).findById(anyLong());
    }

    @Test
    void saveContact() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);
        User recipient = new User();
        recipient.setEmail(RECIPIENT_EMAIL);
        recipient.setId(RECIPIENT_ID);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(payer));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(recipient));
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setDeleted(true);
        when(payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean())).thenReturn(Optional.of(payerRecipient));

        // Act
        contactService.saveContact(recipient.getEmail());

        // Assert
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        verify(userRepository, Mockito.times(1)).findByEmail(anyString());
        verify(payerRecipientRepository, Mockito.times(1)).save(any(PayerRecipient.class));
    }


    @Test
    void saveExistingContact() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);
        User recipient = new User();
        recipient.setEmail(RECIPIENT_EMAIL);
        recipient.setId(RECIPIENT_ID);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(payer));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(recipient));
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setDeleted(false);
        when(payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean())).thenReturn(Optional.of(payerRecipient));

        // Act
        assertThrows(PayerRecipientAlreadyExistsException.class, () -> contactService.saveContact(recipient.getEmail()));

        // Assert
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        verify(userRepository, Mockito.times(1)).findByEmail(anyString());
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean());
        verify(payerRecipientRepository, Mockito.never()).save(any(PayerRecipient.class));
    }

    @Test
    void saveNewContact() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);
        User recipient = new User();
        recipient.setEmail(RECIPIENT_EMAIL);
        recipient.setId(RECIPIENT_ID);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(payer));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(recipient));
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setDeleted(true);
        payerRecipient.setPayerId(PAYER_ID);
        payerRecipient.setRecipientId(RECIPIENT_ID);
        when(payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean())).thenReturn(Optional.empty());

        // Act
        contactService.saveContact(recipient.getEmail());

        // Assert
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        verify(userRepository, Mockito.times(1)).findByEmail(anyString());
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean());
        verify(payerRecipientRepository, Mockito.times(1)).save(any(PayerRecipient.class));
    }


    @Test
    void saveCurrentUserContact() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);
        ;
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(payer));

        // Act
        assertThrows(ContactCannotBeCurrentUserException.class, () -> contactService.saveContact(payer.getEmail()));

        // Assert
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        verify(userRepository, Mockito.never()).findByEmail(anyString());
        verify(payerRecipientRepository, Mockito.never()).findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean());
        verify(payerRecipientRepository, Mockito.never()).save(any(PayerRecipient.class));
    }

    @Test
    void deleteContact() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);
        User recipient = new User();
        recipient.setEmail(RECIPIENT_EMAIL);
        recipient.setId(RECIPIENT_ID);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(payer)).thenReturn(Optional.of(recipient));
        when(payerRecipientRepository.findById(any(PayerRecipientId.class))).thenReturn(Optional.of(new PayerRecipient()));

        // Act
        contactService.deleteContact(RECIPIENT_ID);

        // Assert
        verify(userRepository, Mockito.times(2)).findById(anyLong());
        verify(payerRecipientRepository, Mockito.times(1)).findById(any(PayerRecipientId.class));
    }

    @Test
    void deleteContactUnknownPayer() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> contactService.deleteContact(RECIPIENT_ID));

        // Assert
        verify(userRepository, Mockito.times(1)).findById(anyLong());
        verify(payerRecipientRepository, Mockito.never()).findById(any(PayerRecipientId.class));
    }

    @Test
    void deleteContactUnknownRecipient() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(payer)).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> contactService.deleteContact(RECIPIENT_ID));

        // Assert
        verify(userRepository, Mockito.times(2)).findById(anyLong());
        verify(payerRecipientRepository, Mockito.never()).findById(any(PayerRecipientId.class));
    }

    @Test
    void deleteUnknownContact() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);
        User recipient = new User();
        recipient.setEmail(RECIPIENT_EMAIL);
        recipient.setId(RECIPIENT_ID);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(payer)).thenReturn(Optional.of(recipient));
        when(payerRecipientRepository.findById(any(PayerRecipientId.class))).thenReturn(Optional.empty());

        // Act
        assertThrows(PayerRecipientNotFoundException.class, () -> contactService.deleteContact(RECIPIENT_ID));

        // Assert
        verify(userRepository, Mockito.times(2)).findById(anyLong());
        verify(payerRecipientRepository, Mockito.times(1)).findById(any(PayerRecipientId.class));
    }
}