package com.openclassrooms.PayMyBuddy.integration.controller;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.TransactionDto;
import com.openclassrooms.PayMyBuddy.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionService service;

    @MockBean
    private IContactService contactService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void transactions() throws Exception {
        mockMvc.perform(get("/transaction"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("transactions"))
                .andExpect(content().string(containsString("Ajouter un contact")))
                .andExpect(content().string(containsString("Mes virements")));
        verify(service, times(1)).getAll();
        verify(contactService, times(1)).getContacts();
    }


    @Test
    void saveNewTransaction() throws Exception {
        when(contactService.getContacts()).thenReturn(Collections.singletonList(new ContactDto()));
        when(service.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("accountId=3&amount=2&description=test")).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/transaction"));
        verify(service, Mockito.never()).getAll();
        verify(service, Mockito.times(1)).saveTransaction(any(TransactionDto.class));
        verify(contactService, never()).getContacts();
    }

    @Test
    void saveNewTransactionFormError() throws Exception {
        when(contactService.getContacts()).thenReturn(Collections.singletonList(new ContactDto()));
        when(service.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("contactId=&amount=&description=test")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("transactions"));
        verify(service, Mockito.times(1)).getAll();
        verify(service, Mockito.never()).saveTransaction(any(TransactionDto.class));
        verify(contactService, times(1)).getContacts();
    }

    @Test
    void saveNewTransactionUserNotFoundException() throws Exception {
        doThrow(new UserNotFoundException()).when(service).saveTransaction(any(TransactionDto.class));
        when(contactService.getContacts()).thenReturn(Collections.singletonList(new ContactDto()));
        when(service.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("accountId=3&amount=2&description=test")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("transactions")).andExpect(content().string(containsString("L&#39;utilisateur n&#39;a pas pu être trouvé")));
        verify(service, Mockito.times(1)).getAll();
        verify(service, Mockito.times(1)).saveTransaction(any(TransactionDto.class));
        verify(contactService, times(1)).getContacts();
    }

    @Test
    void saveNewTransactionInsufficientBalanceException() throws Exception {
        doThrow(new InsufficientBalanceException()).when(service).saveTransaction(any(TransactionDto.class));
        when(contactService.getContacts()).thenReturn(Collections.singletonList(new ContactDto()));
        when(service.getAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/transaction").contentType(MediaType.APPLICATION_FORM_URLENCODED).content("accountId=3&amount=2&description=test")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("transactions")).andExpect(content().string(containsString("Le solde est insuffisant.")));
        verify(service, Mockito.times(1)).getAll();
        verify(service, Mockito.times(1)).saveTransaction(any(TransactionDto.class));
        verify(contactService, times(1)).getContacts();
    }

}