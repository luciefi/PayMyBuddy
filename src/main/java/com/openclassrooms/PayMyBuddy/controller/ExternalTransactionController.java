package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.ExternalTransaction;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
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
    private BankAccountService service;


    @GetMapping("/externalTransaction")
    public String externalTransactions(Model model) {
        Iterable<ExternalTransaction> externalTransactions = null;
        model.addAttribute("externalTransactions", externalTransactions);
        return "externalTransactions";
    }

    @GetMapping("/newExternalTransaction")
    public String createExternalTransaction(Model model) {
        ExternalTransaction externalTransaction = new ExternalTransaction();
        model.addAttribute("externalTransaction", externalTransaction);
        return "newExternalTransaction";
    }

    @PostMapping("/newExternalTransaction")
    public String saveNewExternalTransaction(@Valid ExternalTransaction externalTransaction, BindingResult result) {
        if (result.hasErrors()) {
            return "newExternalTransaction";
        }
        try {
            service.saveExternalTransaction(externalTransaction);
            return "redirect:/externalTransaction";
        } catch (Exception e) {  // TODO préciser type
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "newExternalTransaction";
        }
    }
}
