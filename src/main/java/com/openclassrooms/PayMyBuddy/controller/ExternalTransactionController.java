package com.openclassrooms.PayMyBuddy.controller;

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
        Iterable<BankAccount> bankAccounts = bankAccountService.getAllForCurrentUser();
        model.addAttribute("bankAccounts", bankAccounts);
        float balance = userService.getBalance();
        model.addAttribute("balance", balance);
        return "createExternalTransaction";
    }

    @PostMapping("/newExternalTransaction")
    public String saveNewExternalTransaction(@Valid ExternalTransactionDto externalTransactionDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Iterable<BankAccount> bankAccounts = bankAccountService.getAllForCurrentUser();
            model.addAttribute("bankAccounts", bankAccounts);
            float balance = userService.getBalance();
            model.addAttribute("balance", balance);
            return "createExternalTransaction";
        }
        try {
            service.saveExternalTransaction(externalTransactionDto);
            return "redirect:/externalTransaction";
        } catch (Exception e) {  // TODO préciser type
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "createExternalTransaction";
        }
    }
}
