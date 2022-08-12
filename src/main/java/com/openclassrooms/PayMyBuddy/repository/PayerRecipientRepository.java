package com.openclassrooms.PayMyBuddy.repository;


import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.PayerRecipient;
import com.openclassrooms.PayMyBuddy.model.PayerRecipientId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PayerRecipientRepository extends CrudRepository<PayerRecipient, PayerRecipientId> {
    List<PayerRecipient> findByPayerId(Long payerId);
}
