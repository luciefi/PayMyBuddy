package com.openclassrooms.PayMyBuddy.service;

import com.openclassrooms.PayMyBuddy.configuration.WithMockCustomUser;
import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.Transaction;
import com.openclassrooms.PayMyBuddy.model.TransactionDto;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.repository.TransactionRepository;
import com.openclassrooms.PayMyBuddy.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration
@WithMockCustomUser
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private ContactService contactService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getAll() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(1L);

        List<Transaction> transactionList = new ArrayList<>();

        Transaction transaction1 = new Transaction();
        transaction1.setPayer(user1);
        transaction1.setRecipient(user2);
        transaction1.setAmount(1d);
        transactionList.add(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setPayer(user2);
        transaction2.setRecipient(user1);
        transaction2.setAmount(2d);
        transactionList.add(transaction2);

        when(transactionRepository.findByPayerIdOrRecipientIdOrderByTimestampDesc(anyLong(), anyLong(), any(Pageable.class))).thenReturn(new PageImpl<>(transactionList));

        // Act
        Page<TransactionDto> transactionDtos = transactionService.getAllPaginated(1);

        // Assert
        assertNotNull(transactionDtos);
        assertEquals(2, transactionDtos.getNumberOfElements());
    }

    @Test
    void saveTransaction() throws InsufficientBalanceException {
        // Arrange
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setContactId(2L);
        transactionDto.setAmount(1d);
        User user = new User();
        when(userService.getUser(anyLong())).thenReturn(user);

        // Act
        transactionService.saveTransaction(transactionDto);

        // Assert
        verify(userService, times(1)).getUser(anyLong());
        verify(userService, times(1)).debitBalance(anyDouble());
        verify(userService, times(1)).creditBalance(anyDouble(), anyLong());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}