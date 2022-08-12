package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    @GetMapping("/bankAccount")
    public String bankAccounts(Model model) {
        Iterable<BankAccount> bankAccounts = service.getAll();
        model.addAttribute("bankAccounts", bankAccounts);
        return "bankAccount";
    }

    @GetMapping("/createBankAccount")
    public String createBankAccount(Model model) {
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("bankAccount", bankAccount);
        return "createBankAccount";
    }

    @GetMapping("/updateBankAccount/{id}")
    public String updateBankAccount(@PathVariable("id") final Long id, Model model) {
        BankAccount bankAccount = service.getById(id);
        model.addAttribute("bankAccount", bankAccount);
        return "updateBankAccount";
    }

    @PostMapping("/updateBankAccount")
    public String saveBankAccount(@Valid BankAccount bankAccount, BindingResult result) {
       // Error err = new Error("message d'erreur");
       // throw err;

        if (result.hasErrors()) {
            return "updateBankAccount";
        }
        try {
            service.saveBankAccount(bankAccount);
            return "redirect:/bankAccount";
        } catch (BankAccountAlreadyExistsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "updateBankAccount";
        }
    }

    @PostMapping("/createBankAccount")
    public String saveNewBankAccount(@Valid BankAccount bankAccount, BindingResult result) {
        if (result.hasErrors()) {
            return "createBankAccount";
        }
        try {
            service.saveBankAccount(bankAccount);
            return "redirect:/bankAccount";
        } catch (BankAccountAlreadyExistsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "createBankAccount";
        }
    }

    @GetMapping("/deleteBankAccount/{id}")
    public ModelAndView deleteBankAccount(@PathVariable("id") final Long id) {
        service.deleteBankAccount(id);
        return new ModelAndView("redirect:/bankAccount");
    }
}
