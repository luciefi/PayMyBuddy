package com.openclassrooms.PayMyBuddy.repository;


import com.openclassrooms.PayMyBuddy.model.PayerRecipient;
import com.openclassrooms.PayMyBuddy.model.PayerRecipientId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayerRecipientRepository extends CrudRepository<PayerRecipient, PayerRecipientId> {
    List<PayerRecipient> findByPayerIdAndDeleted(Long payerId, boolean deleted);

    Optional<PayerRecipient> findByPayerIdAndRecipientIdAndDeleted(Long payerId, Long recipientId, boolean b);

    Optional<PayerRecipient> findByPayerIdAndRecipientId(Long currentUserId, Long contactId);
}
