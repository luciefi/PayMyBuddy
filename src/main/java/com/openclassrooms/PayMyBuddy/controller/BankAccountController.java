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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class BankAccountController {

    @Autowired
    private BankAccountService service;

    public static final String NEW_BANK_ACCOUNT_SUCCESS_MESSAGE = "\u2714 Le compte bancaire a été ajouté !";

    public static final String BANK_ACCOUNT_UPDATED_SUCCESS_MESSAGE = "\u2714 Le compte bancaire a été modifié.";

    public static final String BANK_ACCOUNT_DEACTIVATED_SUCCESS_MESSAGE = "\u2714 Le compte bancaire a été désactivé.";

    private static final String BANK_ACCOUNT_ACTIVATED_SUCCESS_MESSAGE = "\u2714 Le compte bancaire a été activé.";

    @GetMapping("/bankAccount")
    public String bankAccounts(Model model) {
        Iterable<BankAccount> bankAccounts = service.getAllForCurrentUser();
        model.addAttribute("bankAccounts", bankAccounts);
        for(BankAccount bankAccount : bankAccounts){
            model.addAttribute("bankAccount"+bankAccount.getId(), bankAccount);
        }
        BankAccount newBankAccount = new BankAccount();
        model.addAttribute("newBankAccount", newBankAccount);
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

    @PostMapping("/updateBankAccount/{id}")
    public String saveBankAccount(@Valid BankAccount bankAccount, BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "updateBankAccount";
        }
        try {
            service.saveBankAccount(bankAccount);
            redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_UPDATED_SUCCESS_MESSAGE);
            return "redirect:/bankAccount";
        } catch (BankAccountAlreadyExistsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "updateBankAccount";
        }
    }

    @PostMapping("/createBankAccount")
    public String saveNewBankAccount(@Valid BankAccount bankAccount, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "createBankAccount";
        }
        try {
            service.saveBankAccount(bankAccount);
            redirectAttributes.addFlashAttribute("message", NEW_BANK_ACCOUNT_SUCCESS_MESSAGE);
            return "redirect:/bankAccount";
        } catch (BankAccountAlreadyExistsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "createBankAccount";
        }
    }

    @GetMapping("/deleteBankAccount/{id}")
    public ModelAndView deleteBankAccount(@PathVariable("id") final Long id, RedirectAttributes redirectAttributes) {
        service.deleteBankAccount(id);
        redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_DEACTIVATED_SUCCESS_MESSAGE);
        return new ModelAndView("redirect:/bankAccount");
    }

    @GetMapping("/activateBankAccount/{id}")
    public ModelAndView activateBankAccount(@PathVariable("id") final Long id, RedirectAttributes redirectAttributes) {
        service.activateBankAccount(id);
        redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_ACTIVATED_SUCCESS_MESSAGE);
        return new ModelAndView("redirect:/bankAccount");
    }

}
