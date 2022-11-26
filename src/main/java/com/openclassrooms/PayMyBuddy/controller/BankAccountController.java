package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.service.IBankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class BankAccountController {

    @Autowired
    private IBankAccountService service;

    Logger logger = LoggerFactory.getLogger(BankAccountController.class);

    public static final String NEW_BANK_ACCOUNT_SUCCESS_MESSAGE = "\u2714 \u2007 Le compte bancaire a été ajouté !";

    public static final String BANK_ACCOUNT_UPDATED_SUCCESS_MESSAGE = "\u2714 \u2007 Le compte bancaire a été modifié.";

    public static final String BANK_ACCOUNT_DEACTIVATED_SUCCESS_MESSAGE = "\u2714 \u2007 Le compte bancaire a été désactivé.";

    private static final String BANK_ACCOUNT_ACTIVATED_SUCCESS_MESSAGE = "\u2714 \u2007 Le compte bancaire a été activé.";

    @GetMapping("/bankAccount")
    public String bankAccounts(Model model) {
        addAttributesToModel(model);
        return "bankAccount";
    }

    @PostMapping("/updateBankAccount/{id}")
    public String saveBankAccount(@Valid BankAccount bankAccountToUpdate, BindingResult result, Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.info("Cannot update bank account : invalid form");
            addAttributesToModel(model);
            return "bankAccount";
        }
        try {
            service.saveBankAccount(bankAccountToUpdate);
            logger.info("Bank account updated");
            redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_UPDATED_SUCCESS_MESSAGE);
            return "redirect:/bankAccount";
        } catch (BankAccountAlreadyExistsException e) {
            logger.info("Cannot update bank account : " + e.getMessage());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            addAttributesToModel(model);
            return "bankAccount";
        }
    }

    @PostMapping("/createBankAccount")
    public String saveNewBankAccount(@Valid BankAccount bankAccount, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update bank account : invalid form");
            Iterable<BankAccount> bankAccounts = service.getPaginatedForCurrentUser();
            model.addAttribute("bankAccounts", bankAccounts);
            for (BankAccount bankAccountItem : bankAccounts) {
                model.addAttribute("bankAccount" + bankAccountItem.getId(), bankAccountItem);
            }
            return "bankAccount";
        }
        try {
            service.saveBankAccount(bankAccount);
            logger.info("Bank account created");
            redirectAttributes.addFlashAttribute("message", NEW_BANK_ACCOUNT_SUCCESS_MESSAGE);
            return "redirect:/bankAccount";
        } catch (BankAccountAlreadyExistsException e) {
            logger.info("Cannot update bank account : " + e.getMessage());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            Iterable<BankAccount> bankAccounts = service.getPaginatedForCurrentUser();
            model.addAttribute("bankAccounts", bankAccounts);
            for (BankAccount bankAccountItem : bankAccounts) {
                model.addAttribute("bankAccount" + bankAccountItem.getId(), bankAccountItem);
            }
            return "bankAccount";
        }
    }

    @GetMapping("/deleteBankAccount/{id}")
    public ModelAndView deleteBankAccount(@PathVariable("id") final Long id, RedirectAttributes redirectAttributes) {
        service.deleteBankAccount(id);
        logger.info("Bank account deleted");
        redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_DEACTIVATED_SUCCESS_MESSAGE);
        return new ModelAndView("redirect:/bankAccount");
    }

    @GetMapping("/activateBankAccount/{id}")
    public ModelAndView activateBankAccount(@PathVariable("id") final Long id, RedirectAttributes redirectAttributes) {
        service.activateBankAccount(id);
        logger.info("Bank account activated");
        redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_ACTIVATED_SUCCESS_MESSAGE);
        return new ModelAndView("redirect:/bankAccount");
    }

    private void addAttributesToModel(Model model) {
        Iterable<BankAccount> bankAccounts = service.getPaginatedForCurrentUser();
        model.addAttribute("bankAccounts", bankAccounts);
        for (BankAccount bankAccount : bankAccounts) {
            model.addAttribute("bankAccount" + bankAccount.getId(), bankAccount);
        }
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("bankAccount", bankAccount);
    }
}
