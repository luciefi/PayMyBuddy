package com.openclassrooms.PayMyBuddy.repository;


import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.PayerRecipient;
import com.openclassrooms.PayMyBuddy.model.PayerRecipientId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PayerRecipientRepository extends CrudRepository<PayerRecipient, PayerRecipientId> {
    List<PayerRecipient> findByPayerIdAndDeleted(Long payerId, boolean deleted);

    Optional<PayerRecipient> findByPayerIdAndRecipientIdAndDeleted(Long payerId, Long recipientId, boolean b);
}
