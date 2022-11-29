package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.configuration.WithMockCustomUser;
import com.openclassrooms.PayMyBuddy.exception.ContactCannotBeCurrentUserException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientNotFoundException;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.PayerRecipient;
import com.openclassrooms.PayMyBuddy.model.PayerRecipientId;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.PayerRecipientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration
@WithMockCustomUser
class ContactServiceTest {

    @Mock
    private PayerRecipientRepository payerRecipientRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContactService contactService;

    final Long PAYER_ID = 1L;
    final Long RECIPIENT_ID = 2L;

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
        when(userService.getUser(anyLong())).thenReturn(recipient);

        // Act
        List<ContactDto> contacts = (List<ContactDto>) contactService.getContacts();

        // Assert
        assertThat(contacts).isNotNull();
        assertThat(contacts.size()).isEqualTo(1);
        assertThat(contacts.get(0).getEmail()).isEqualTo(RECIPIENT_EMAIL);
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndDeleted(anyLong(), anyBoolean());
        verify(userService, Mockito.times(1)).getUser(anyLong());
    }

    @Test
    void getPaginatedContacts() {
        // Arrange
        PayerRecipient payerRecipient = new PayerRecipient();
        User recipient = new User();
        recipient.setId(RECIPIENT_ID);
        recipient.setEmail(RECIPIENT_EMAIL);
        payerRecipient.setRecipient(recipient);
        when(payerRecipientRepository.findByPayerIdAndDeleted(anyLong(), anyBoolean(), any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(payerRecipient)));
        when(userService.getUser(anyLong())).thenReturn(recipient);

        // Act
        Page<ContactDto> contacts = contactService.getPaginatedContacts(0);

        // Assert
        assertThat(contacts).isNotNull();
        assertThat(contacts.getNumberOfElements()).isEqualTo(1);
        assertThat(contacts.getContent().get(0).getEmail()).isEqualTo(RECIPIENT_EMAIL);
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndDeleted(anyLong(), anyBoolean(), any(Pageable.class));
        verify(userService, Mockito.times(1)).getUser(anyLong());
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
        when(userService.getUser(anyLong())).thenReturn(payer);
        when(userService.getUserByEmail(anyString())).thenReturn(recipient);
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setDeleted(true);
        when(payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean())).thenReturn(Optional.of(payerRecipient));

        // Act
        contactService.saveContact(recipient.getEmail());

        // Assert
        verify(userService, Mockito.times(1)).getUser(anyLong());
        verify(userService, Mockito.times(1)).getUserByEmail(anyString());
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
        when(userService.getUser(anyLong())).thenReturn(payer);
        when(userService.getUserByEmail(anyString())).thenReturn(recipient);
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setDeleted(false);
        when(payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean())).thenReturn(Optional.of(payerRecipient));

        // Act
        assertThrows(PayerRecipientAlreadyExistsException.class, () -> contactService.saveContact(recipient.getEmail()));

        // Assert
        verify(userService, Mockito.times(1)).getUser(anyLong());
        verify(userService, Mockito.times(1)).getUserByEmail(anyString());
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
        when(userService.getUser(anyLong())).thenReturn(payer);
        when(userService.getUserByEmail(anyString())).thenReturn(recipient);
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setDeleted(true);
        payerRecipient.setPayerId(PAYER_ID);
        payerRecipient.setRecipientId(RECIPIENT_ID);
        when(payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean())).thenReturn(Optional.empty());

        // Act
        contactService.saveContact(recipient.getEmail());

        // Assert
        verify(userService, Mockito.times(1)).getUser(anyLong());
        verify(userService, Mockito.times(1)).getUserByEmail(anyString());
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndRecipientIdAndDeleted(anyLong(), anyLong(), anyBoolean());
        verify(payerRecipientRepository, Mockito.times(1)).save(any(PayerRecipient.class));
    }


    @Test
    void saveCurrentUserContact() {
        // Arrange
        User payer = new User();
        payer.setEmail(PAYER_EMAIL);
        payer.setId(PAYER_ID);

        when(userService.getUser(anyLong())).thenReturn(payer);

        // Act
        assertThrows(ContactCannotBeCurrentUserException.class, () -> contactService.saveContact(payer.getEmail()));

        // Assert
        verify(userService, Mockito.times(1)).getUser(anyLong());
        verify(userService, Mockito.never()).getUserByEmail(anyString());
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
        when(userService.getUser(anyLong())).thenReturn(payer).thenReturn(recipient);
        when(payerRecipientRepository.findById(any(PayerRecipientId.class))).thenReturn(Optional.of(new PayerRecipient()));

        // Act
        contactService.deleteContact(RECIPIENT_ID);

        // Assert
        verify(userService, Mockito.times(2)).getUser(anyLong());
        verify(payerRecipientRepository, Mockito.times(1)).findById(any(PayerRecipientId.class));
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
        when(userService.getUser(anyLong())).thenReturn(payer).thenReturn(recipient);
        when(payerRecipientRepository.findById(any(PayerRecipientId.class))).thenReturn(Optional.empty());

        // Act
        assertThrows(PayerRecipientNotFoundException.class, () -> contactService.deleteContact(RECIPIENT_ID));

        // Assert
        verify(userService, Mockito.times(2)).getUser(anyLong());
        verify(payerRecipientRepository, Mockito.times(1)).findById(any(PayerRecipientId.class));
    }

    @Test
    void updateLastTransactionDateTest() {
        // Arrange
        when(payerRecipientRepository.findByPayerIdAndRecipientId(anyLong(), anyLong())).thenReturn(Optional.of(new PayerRecipient()));

        // Act
        contactService.updateLastTransactionDate(RECIPIENT_ID);

        // Assert
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndRecipientId(anyLong(), anyLong());
        verify(payerRecipientRepository, Mockito.times(1)).save(any(PayerRecipient.class));
    }

    @Test
    void updateLastTransactionDateUserNotFoundTest() {
        // Arrange
        when(payerRecipientRepository.findByPayerIdAndRecipientId(anyLong(), anyLong())).thenReturn(Optional.empty());

        // Act
        assertThrows(PayerRecipientNotFoundException.class, () -> contactService.updateLastTransactionDate(RECIPIENT_ID));

        // Assert
        verify(payerRecipientRepository, Mockito.times(1)).findByPayerIdAndRecipientId(anyLong(), anyLong());
        verify(payerRecipientRepository, Mockito.times(0)).save(any(PayerRecipient.class));
    }
}