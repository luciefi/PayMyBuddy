package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.*;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.repository.PayerRecipientRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import com.openclassrooms.PayMyBuddy.utils.ContactUtils;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService implements IContactService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PayerRecipientRepository payerRecipientRepository;

    @Override
    public Iterable<ContactDto> getContacts() {
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        List<PayerRecipient> contacts = payerRecipientRepository.findByPayerIdAndDeleted(currentUserId, false);
        List<ContactDto> contactDtoList = contacts.stream().map(payerRecipient -> {
            User user = userRepository.findById(payerRecipient.getRecipient().getId()).orElseThrow(UserNotFoundException::new);
            return ContactUtils.convertToContactDto(user, payerRecipient);
        }).collect(Collectors.toList());
        return contactDtoList;
    }

    @Override
    public String saveContact(String emailAddress) {
        User payer = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(CurrentUserNotFoundException::new);
        if (payer.getEmail().equals(emailAddress)) {
            throw new ContactCannotBeCurrentUserException();
        }

        User recipient = userRepository.findByEmail(emailAddress).orElseThrow(UserNotFoundException::new);
        Optional<PayerRecipient> existingContact = getExistingContact(payer, recipient);
        PayerRecipient payerRecipient = existingContact.map(this::undeleteContact).orElseGet(() -> createContact(payer, recipient));
        payerRecipientRepository.save(payerRecipient);
        return emailAddress;
    }

    private PayerRecipient undeleteContact(PayerRecipient payerRecipient) {
        if (!payerRecipient.isDeleted()) {
            throw new PayerRecipientAlreadyExistsException();
        }
        payerRecipient.setDeleted(false);
        return payerRecipient;
    }

    private Optional<PayerRecipient> getExistingContact(User payer, User recipient) {
        Optional<PayerRecipient> payerRecipient = payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(payer.getId(), recipient.getId(), false);
        return payerRecipient;
    }

    @Override
    public void deleteContact(String email) {
        User payer = userRepository.findById(CurrentUserUtils.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        User recipient = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        PayerRecipientId payerRecipientId = new PayerRecipientId(payer.getId(), recipient.getId());
        PayerRecipient payerRecipient = payerRecipientRepository.findById(payerRecipientId).orElseThrow(PayerRecipientNotFoundException::new);
        payerRecipient.setDeleted(true);
        payerRecipientRepository.save(payerRecipient);
    }

    private PayerRecipient createContact(User payer, User recipient) {
        PayerRecipient payerRecipient = new PayerRecipient();
        payerRecipient.setPayerId(payer.getId());
        payerRecipient.setRecipientId(recipient.getId());
        Calendar calendar = Calendar.getInstance();
        payerRecipient.setDateOfCreation(new Timestamp(calendar.getTime().getTime()));
        payerRecipient.setDeleted(false);
        return payerRecipient;
    }

}
