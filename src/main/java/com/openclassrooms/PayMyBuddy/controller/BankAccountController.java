package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.service.IBankAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

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
    public String bankAccounts(Model model, @RequestParam(name = "page", required = false) Optional<Integer> page) {
        addAttributesToModel(model, page.orElse(1));
        return "bankAccount";
    }

    @PostMapping("/updateBankAccount/{id}")
    public String saveBankAccount(@Valid BankAccount bankAccountToUpdate, BindingResult result, Model model, RedirectAttributes redirectAttributes, @RequestParam(name = "page", required = false) Optional<Integer> page) {
        if (result.hasErrors()) {
            logger.info("Cannot update bank account : invalid form");
            addAttributesToModel(model, page.orElse(1));
            return "bankAccount";
        }
        try {
            service.saveBankAccount(bankAccountToUpdate);
            logger.info("Bank account updated");
            redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_UPDATED_SUCCESS_MESSAGE);
            return page.map(integer -> "redirect:/bankAccount?page=" + integer).orElse("redirect:/bankAccount");
        } catch (BankAccountAlreadyExistsException e) {
            logger.info("Cannot update bank account : " + e.getMessage());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            addAttributesToModel(model, page.orElse(1));
            return "bankAccount";
        }
    }

    @PostMapping("/createBankAccount")
    public String saveNewBankAccount(@Valid BankAccount bankAccount, BindingResult result, RedirectAttributes redirectAttributes, Model model, @RequestParam(name = "page", required = false) Optional<Integer> page) {
        if (result.hasErrors()) {
            logger.info("Cannot update bank account : invalid form");
            int pageNumber = page.orElse(1) - 1;
            Page<BankAccount> bankAccountPage = service.getPaginatedForCurrentUser(Math.max(0, pageNumber));
            model.addAttribute("bankAccountPage", bankAccountPage);
            for (BankAccount bankAccountItem : bankAccountPage) {
                model.addAttribute("bankAccount" + bankAccountItem.getId(), bankAccountItem);
            }
            return "bankAccount";
        }
        try {
            service.saveBankAccount(bankAccount);
            logger.info("Bank account created");
            redirectAttributes.addFlashAttribute("message", NEW_BANK_ACCOUNT_SUCCESS_MESSAGE);
            return page.map(integer -> "redirect:/bankAccount?page=" + integer).orElse("redirect:/bankAccount");
        } catch (BankAccountAlreadyExistsException e) {
            logger.info("Cannot update bank account : " + e.getMessage());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            int pageNumber = page.orElse(1) - 1;
            Page<BankAccount> bankAccountPage = service.getPaginatedForCurrentUser(Math.max(0, pageNumber));
            model.addAttribute("bankAccountPage", bankAccountPage);
            for (BankAccount bankAccountItem : bankAccountPage) {
                model.addAttribute("bankAccount" + bankAccountItem.getId(), bankAccountItem);
            }
            return "bankAccount";
        }
    }

    @GetMapping("/deleteBankAccount/{id}")
    public String deleteBankAccount(@PathVariable("id") final Long id, RedirectAttributes redirectAttributes, @RequestParam(name = "page", required =
            false) Optional<Integer> page) {
        service.deleteBankAccount(id);
        logger.info("Bank account deleted");
        redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_DEACTIVATED_SUCCESS_MESSAGE);
        return page.map(integer -> "redirect:/bankAccount?page=" + integer).orElse("redirect:/bankAccount");
    }

    @GetMapping("/activateBankAccount/{id}")
    public String activateBankAccount(@PathVariable("id") final Long id, RedirectAttributes redirectAttributes, @RequestParam(name = "page",
            required = false) Optional<Integer> page) {
        service.activateBankAccount(id);
        logger.info("Bank account activated");
        redirectAttributes.addFlashAttribute("message", BANK_ACCOUNT_ACTIVATED_SUCCESS_MESSAGE);
        return page.map(integer -> "redirect:/bankAccount?page=" + integer).orElse("redirect:/bankAccount");
    }

    private void addAttributesToModel(Model model, int pageNumber) {
        Page<BankAccount> bankAccountPage = service.getPaginatedForCurrentUser(Math.max(0, pageNumber - 1));
        model.addAttribute("bankAccountPage", bankAccountPage);
        for (BankAccount bankAccount : bankAccountPage) {
            model.addAttribute("bankAccount" + bankAccount.getId(), bankAccount);
        }
        BankAccount bankAccount = new BankAccount();
        model.addAttribute("bankAccount", bankAccount);
    }
}
