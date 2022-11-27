package com.openclassrooms.PayMyBuddy.integration.controller;

import com.openclassrooms.PayMyBuddy.exception.ContactCannotBeCurrentUserException;
import com.openclassrooms.PayMyBuddy.exception.CurrentUserNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientAlreadyExistsException;

import com.openclassrooms.PayMyBuddy.service.ContactService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ContactControllerTest {

    @MockBean
    ContactService contactService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contacts() throws Exception {
        when(contactService.getPaginatedContacts(anyInt())).thenReturn(Page.empty());
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
        verify(contactService, Mockito.times(0)).getPaginatedContacts(anyInt());
    }


    @Test
    void createNewContactHasError() throws Exception {
        when(contactService.getContacts()).thenReturn(Collections.EMPTY_LIST);
        when(contactService.getPaginatedContacts(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("ne peut pas être vide")));
        verify(contactService, Mockito.times(1)).getPaginatedContacts(anyInt());
        verify(contactService, Mockito.times(0)).saveContact(anyString());
    }

    @Test
    void createNewContactUserNotFound() throws Exception {
        when(contactService.saveContact(anyString())).thenThrow(new CurrentUserNotFoundException());
        when(contactService.getPaginatedContacts(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=abcd@efg.hi")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("a pas pu être trouvé")));
        verify(contactService, Mockito.times(1)).saveContact(anyString());
        verify(contactService, Mockito.times(1)).getPaginatedContacts(anyInt());
    }

    @Test
    void createNewContactPayerRecipientAlreadyExistsException() throws Exception {
        when(contactService.saveContact(anyString())).thenThrow(new PayerRecipientAlreadyExistsException());
        when(contactService.getPaginatedContacts(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=abcd@efg.hi")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("Ce contact existe déjà.")));
        verify(contactService, Mockito.times(1)).saveContact(anyString());
        verify(contactService, Mockito.times(1)).getPaginatedContacts(anyInt());
    }

    @Test
    void createNewContactContactCannotBeCurrentUserException() throws Exception {
        when(contactService.saveContact(anyString())).thenThrow(new ContactCannotBeCurrentUserException());
        when(contactService.getPaginatedContacts(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(post("/contact")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("emailAddress=abcd@efg.hi")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(content().string(containsString("email doit être différent du vôtre")));
        verify(contactService, Mockito.times(1)).saveContact(anyString());
        verify(contactService, Mockito.times(1)).getPaginatedContacts(anyInt());
    }

    @Test
    void deleteContact() throws Exception {
        mockMvc.perform(get("/deleteContact/1")).andExpect(status().isFound());
        verify(contactService, Mockito.times(1)).deleteContact(any());
    }
}