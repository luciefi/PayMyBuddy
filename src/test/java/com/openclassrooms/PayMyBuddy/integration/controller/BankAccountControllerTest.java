package com.openclassrooms.PayMyBuddy.integration.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BankAccountControllerTest {

    @MockBean
    BankAccountService bankAccountService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getBankAccountsTest() throws Exception {
        mockMvc.perform(get("/bankAccount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("bankAccount"))
                .andExpect(content().string(containsString("compte")));
    }

    @Test
    public void createBankAccountTest() throws Exception {
        mockMvc.perform(get("/createBankAccount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createBankAccount"))
                .andExpect(content().string(containsString("Ajout")));
    }

    @Test
    public void createBankAccountPostTest() throws Exception {
        mockMvc.perform(post("/createBankAccount")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("name=compte&iban=2123456677899000&bic=12390003")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/bankAccount"));
        verify(bankAccountService, Mockito.times(1)).saveBankAccount(any(BankAccount.class));
    }

    @Test
    public void createBankAccountPostFormErrorTest() throws Exception {
        mockMvc.perform(post("/createBankAccount")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("name=&iban=&bic=")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createBankAccount"));
        verify(bankAccountService, Mockito.times(0)).saveBankAccount(any(BankAccount.class));
    }

    @Test
    public void createBankAccountPostAlreadyExistsTest() throws Exception {
        when(bankAccountService.saveBankAccount(any(BankAccount.class))).thenThrow(new BankAccountAlreadyExistsException());
        mockMvc.perform(post("/createBankAccount")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("name=compte&iban=2123456677899000&bic=12390003")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("createBankAccount"));
        verify(bankAccountService, Mockito.times(1)).saveBankAccount(any(BankAccount.class));
    }

    @Test
    public void updateBankAccountTest() throws Exception {
        when(bankAccountService.getById(any(Long.class))).thenReturn(new BankAccount());
        mockMvc.perform(get("/updateBankAccount/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateBankAccount"))
                .andExpect(content().string(containsString("Modifier")));
        verify(bankAccountService, Mockito.times(1)).getById(any(Long.class));
    }

    @Test
    public void updateBankAccountPostTest() throws Exception {
        mockMvc.perform(post("/updateBankAccount")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("name=compte&iban=2123456677899000&bic=12390003")
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/bankAccount"));
        verify(bankAccountService, Mockito.times(1)).saveBankAccount(any(BankAccount.class));
    }

    @Test
    public void updateBankAccountPostFormErrorTest() throws Exception {
        mockMvc.perform(post("/updateBankAccount")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("name=&iban=&bic=")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateBankAccount"));
        verify(bankAccountService, Mockito.times(0)).saveBankAccount(any(BankAccount.class));
    }

    @Test
    public void updateBankAccountPostAlreadyExistsTest() throws Exception {
        when(bankAccountService.saveBankAccount(any(BankAccount.class))).thenThrow(new BankAccountAlreadyExistsException());
        mockMvc.perform(post("/updateBankAccount")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("name=compte&iban=2123456677899000&bic=12390003")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateBankAccount"));
        verify(bankAccountService, Mockito.times(1)).saveBankAccount(any(BankAccount.class));
    }

    @Test
    void deleteBankAccount() throws Exception {
        mockMvc.perform(get("/deleteBankAccount/1")).andExpect(status().isFound());
        verify(bankAccountService, Mockito.times(1)).deleteBankAccount(any());
    }
}
