package com.openclassrooms.PayMyBuddy.integration.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.ContactCannotBeCurrentUserException;
import com.openclassrooms.PayMyBuddy.exception.CurrentUserNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.EmailAddress;
import com.openclassrooms.PayMyBuddy.model.UserDto;
import com.openclassrooms.PayMyBuddy.service.ContactService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @MockBean
    ContactService contactService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contacts() throws Exception {
        mockMvc.perform(get("/contact"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("Mes contacts")));
    }

    @Test
    void createNewContact() throws Exception {
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=abcd@efg.hi")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/contact"));
        verify(contactService, Mockito.times(1)).saveContact(anyString());
        verify(contactService, Mockito.times(0)).getContacts();
    }


    @Test
    void createNewContactHasError() throws Exception {
        when(contactService.getContacts()).thenReturn(Collections.EMPTY_LIST);
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("ne peut pas être vide")));
        verify(contactService, Mockito.times(1)).getContacts();
        verify(contactService, Mockito.times(0)).saveContact(anyString());
    }

    @Test
    void createNewContactUserNotFound() throws Exception {
        when(contactService.saveContact(anyString())).thenThrow(new CurrentUserNotFoundException());
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=abcd@efg.hi")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("a pas pu être trouvé")));
        verify(contactService, Mockito.times(1)).saveContact(anyString());
        verify(contactService, Mockito.times(1)).getContacts();
    }

    @Test
    void createNewContactPayerRecipientAlreadyExistsException() throws Exception {
        when(contactService.saveContact(anyString())).thenThrow(new PayerRecipientAlreadyExistsException());
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=abcd@efg.hi")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("Ce contact existe déjà.")));
        verify(contactService, Mockito.times(1)).saveContact(anyString());
        verify(contactService, Mockito.times(1)).getContacts();
    }

    @Test
    void createNewContactContactCannotBeCurrentUserException() throws Exception {
        when(contactService.saveContact(anyString())).thenThrow(new ContactCannotBeCurrentUserException());
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=abcd@efg.hi")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("email doit être différent du vôtre")));
        verify(contactService, Mockito.times(1)).saveContact(anyString());
        verify(contactService, Mockito.times(1)).getContacts();
    }

    @Test
    void deleteContact() throws Exception {
        mockMvc.perform(get("/deleteContact/1")).andExpect(status().isFound());
        verify(contactService, Mockito.times(1)).deleteContact(any());
    }
}