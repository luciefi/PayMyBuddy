package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
import com.openclassrooms.PayMyBuddy.service.ExternalTransactionService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ExternalTransactionController {

    @Autowired
    private ExternalTransactionService service;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private UserService userService;

    @GetMapping("/externalTransaction")
    public String externalTransactions(Model model) {
        Iterable<ExternalTransactionDto> externalTransactions = service.getAllDto();
        model.addAttribute("externalTransactions", externalTransactions);
        return "externalTransactions";
    }

    @GetMapping("/newExternalTransaction")
    public String createExternalTransaction(Model model) {
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        model.addAttribute("externalTransactionDto", externalTransactionDto);
        addAttributesToModel(model);
        return "createExternalTransaction";
    }

    @PostMapping("/newExternalTransaction")
    public String saveNewExternalTransaction(@Valid ExternalTransactionDto externalTransactionDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addAttributesToModel(model);
            return "createExternalTransaction";
        }
        try {
            service.saveExternalTransaction(externalTransactionDto);
            return "redirect:/externalTransaction";
        } catch (BankAccountNotFoundException | InsufficientBalanceException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            addAttributesToModel(model);
            return "createExternalTransaction";
        }
    }

    private void addAttributesToModel(Model model) {
        Iterable<BankAccount> bankAccounts = bankAccountService.getAllActiveForCurrentUser();
        model.addAttribute("bankAccounts", bankAccounts);
        double balance = userService.getBalance();
        model.addAttribute("balance", balance);
    }
}
