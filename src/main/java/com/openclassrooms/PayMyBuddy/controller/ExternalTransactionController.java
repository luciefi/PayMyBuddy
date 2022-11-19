package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountNotFoundException;
import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ExternalTransactionDto;
import com.openclassrooms.PayMyBuddy.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class ExternalTransactionController {

    @Autowired
    private IExternalTransactionService service;

    @Autowired
    private IBankAccountService bankAccountService;

    @Autowired
    private IUserService userService;

    public static final String NEW_TRANSACTION_SUCCESS_MESSAGE = "Le virement a été effectué !";


    @GetMapping("/externalTransaction")
    public String externalTransactions(Model model) {
        addAttributesToModel(model);
        ExternalTransactionDto externalTransactionDto = new ExternalTransactionDto();
        model.addAttribute("externalTransactionDto", externalTransactionDto);
        return "externalTransactions";
    }

    @PostMapping("/newExternalTransaction")
    public String saveNewExternalTransaction(@Valid ExternalTransactionDto externalTransactionDto, BindingResult result, Model model,
                                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            addAttributesToModel(model);
            return "externalTransactions";
        }
        try {
            service.saveExternalTransaction(externalTransactionDto);
            redirectAttributes.addFlashAttribute("message", NEW_TRANSACTION_SUCCESS_MESSAGE);
            return "redirect:/externalTransaction";
        } catch (BankAccountNotFoundException | InsufficientBalanceException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            addAttributesToModel(model);
            return "externalTransactions";
        }
    }

    private void addAttributesToModel(Model model) {
        Iterable<BankAccount> bankAccounts = bankAccountService.getAllActiveForCurrentUser();
        model.addAttribute("bankAccounts", bankAccounts);
        double balance = userService.getBalance();
        model.addAttribute("balance", balance);
        Iterable<ExternalTransactionDto> externalTransactions = service.getAllDto();
        model.addAttribute("externalTransactions", externalTransactions);
    }
}
