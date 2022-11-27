package com.openclassrooms.PayMyBuddy.integration.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
import com.openclassrooms.PayMyBuddy.service.ExternalTransactionService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
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
class ExternalTransactionControllerTest {

    @MockBean
    BankAccountService bankAccountService;

    @MockBean
    private ExternalTransactionService service;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void externalTransactions() throws Exception {
        when(service.getAllDto(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(get("/externalTransaction"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("externalTransactions"))
                .andExpect(content().string(containsString("Mes virements vers mon compte bancaire")));
        verify(service, times(1)).getAllDto(anyInt());
    }

    @Test
    void saveNewExternalTransaction() throws Exception {
        mockMvc.perform(post("/newExternalTransaction")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("bankAccountId=3&transactionType=0&amount=2&description=test")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/externalTransaction"));
        verify(service, Mockito.times(1)).saveExternalTransaction(any(ExternalTransactionDto.class));
        verify(userService, never()).getBalance();
        verify(bankAccountService, never()).getPaginatedForCurrentUser(1);
    }

    @Test
    void saveNewExternalTransactionFormError() throws Exception {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setDeactivated(false);
        when(bankAccountService.getAllActiveForCurrentUser()).thenReturn(Collections.singletonList(bankAccount));
        when(service.getAllDto(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(post("/newExternalTransaction")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("bankAccountId=&transactionType=&amount=&description=test")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("externalTransactions"));
        verify(service, Mockito.never()).saveExternalTransaction(any(ExternalTransactionDto.class));
        verify(userService, times(1)).getBalance();
        verify(bankAccountService, times(1)).getAllActiveForCurrentUser();
    }


    @Test
    void saveNewExternalTransactionBankAccountNotFoundException() throws Exception {
        doThrow(new BankAccountNotFoundException()).when(service).saveExternalTransaction(any(ExternalTransactionDto.class));
        BankAccount bankAccount = new BankAccount();
        bankAccount.setDeactivated(false);
        when(bankAccountService.getAllActiveForCurrentUser()).thenReturn(Collections.singletonList(bankAccount));
        when(service.getAllDto(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(post("/newExternalTransaction")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("bankAccountId=3&transactionType=0&amount=2&description=test")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("externalTransactions"))
                .andExpect(content().string(containsString("Compte bancaire introuvable.")));
        verify(service, Mockito.times(1)).saveExternalTransaction(any(ExternalTransactionDto.class));
        verify(userService, times(1)).getBalance();
        verify(bankAccountService, times(1)).getAllActiveForCurrentUser();
    }

    @Test
    void saveNewExternalTransactionInsufficientBalanceException() throws Exception {
        doThrow(new InsufficientBalanceException()).when(service).saveExternalTransaction(any(ExternalTransactionDto.class));
        when(service.getAllDto(anyInt())).thenReturn(Page.empty());
        BankAccount bankAccount = new BankAccount();
        bankAccount.setDeactivated(false);
        when(bankAccountService.getAllActiveForCurrentUser()).thenReturn(Collections.singletonList(bankAccount));
        when(service.getAllDto(anyInt())).thenReturn(Page.empty());
        mockMvc.perform(post("/newExternalTransaction")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("bankAccountId=3&transactionType=0&amount=2&description=test")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("externalTransactions"))
                .andExpect(content().string(containsString("Le solde est insuffisant.")));
        verify(service, Mockito.times(1)).saveExternalTransaction(any(ExternalTransactionDto.class));
        verify(userService, times(1)).getBalance();
        verify(bankAccountService, times(1)).getAllActiveForCurrentUser();
    }
}