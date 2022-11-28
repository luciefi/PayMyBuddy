package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.exception.*;
import com.openclassrooms.PayMyBuddy.model.*;
import com.openclassrooms.PayMyBuddy.repository.PayerRecipientRepository;
import com.openclassrooms.PayMyBuddy.utils.ContactUtils;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContactService implements IContactService {

    private static final int CONTACT_PAGE_SIZE = 5;
    @Autowired
    private UserService userService;

    @Autowired
    private PayerRecipientRepository payerRecipientRepository;

    @Override
    public Page<ContactDto> getPaginatedContacts(int pageNumber) {
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        int startItem = pageNumber * CONTACT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(pageNumber, CONTACT_PAGE_SIZE);
        Page<PayerRecipient> contacts = payerRecipientRepository.findByPayerIdAndDeleted(currentUserId, false, pageable);
        return contacts.map(payerRecipient -> {
            User user = userService.getUser(payerRecipient.getRecipient().getId());
            return ContactUtils.convertToContactDto(user, payerRecipient);
        });
    }

    @Override
    public List<ContactDto> getContacts() {
        Long currentUserId = CurrentUserUtils.getCurrentUserId();
        List<PayerRecipient> contacts = payerRecipientRepository.findByPayerIdAndDeleted(currentUserId, false);
        List<ContactDto> contactDtoList = contacts.stream().map(payerRecipient -> {
            User user = userService.getUser(payerRecipient.getRecipient().getId());
            return ContactUtils.convertToContactDto(user, payerRecipient);
        }).collect(Collectors.toList());
        return contactDtoList;
    }

    @Override
    public String saveContact(String emailAddress) {
        User payer = userService.getUser(CurrentUserUtils.getCurrentUserId());
        if (payer.getEmail().equals(emailAddress)) {
            throw new ContactCannotBeCurrentUserException();
        }

        User recipient = userService.getUserByEmail(emailAddress);
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
        return payerRecipientRepository.findByPayerIdAndRecipientIdAndDeleted(payer.getId(), recipient.getId(), false);
    }

    @Override
    public void deleteContact(Long recipientId) {
        User payer = userService.getUser(CurrentUserUtils.getCurrentUserId());
        User recipient = userService.getUser(recipientId);
        PayerRecipientId payerRecipientId = new PayerRecipientId(payer.getId(), recipient.getId());
        PayerRecipient payerRecipient = payerRecipientRepository.findById(payerRecipientId).orElseThrow(PayerRecipientNotFoundException::new);
        payerRecipient.setDeleted(true);
        payerRecipientRepository.save(payerRecipient);
    }

    @Override
    public void updateLastTransactionDate(Long contactId) {
        PayerRecipient payerRecipient =
                payerRecipientRepository.findByPayerIdAndRecipientId(CurrentUserUtils.getCurrentUserId(), contactId).orElseThrow(PayerRecipientNotFoundException::new);
        Calendar calendar = Calendar.getInstance();
        payerRecipient.setLastTransactionDate(new Timestamp(calendar.getTime().getTime()));
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
