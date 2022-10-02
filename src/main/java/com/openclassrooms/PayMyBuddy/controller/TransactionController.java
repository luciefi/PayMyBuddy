package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.TransactionDto;
import com.openclassrooms.PayMyBuddy.service.IContactService;
import com.openclassrooms.PayMyBuddy.service.TransactionService;
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
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private UserService userService;

    @Autowired
    private IContactService contactService;

    @GetMapping("/transaction")
    public String transactions(Model model) {
        addAttributesToModel(model);
        TransactionDto transactionDto = new TransactionDto();
        model.addAttribute("transactionDto", transactionDto);
        return "transactions";
    }

    @PostMapping("/transaction")
    public String saveNewTransaction(@Valid TransactionDto transactionDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            addAttributesToModel(model);
            return "transactions";
        }
        try {
            service.saveTransaction(transactionDto);
            return "redirect:/transaction";
        } catch (UserNotFoundException | InsufficientBalanceException e) { // TODO
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            addAttributesToModel(model);
            return "transactions";
        }
    }

    private void addAttributesToModel(Model model) {
        Iterable<TransactionDto> transactions = service.getAll();
        model.addAttribute("transactions", transactions);
        Iterable<ContactDto> contacts = contactService.getContacts();
        model.addAttribute("contacts", contacts);
    }
}
