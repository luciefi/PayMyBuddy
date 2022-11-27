package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.InsufficientBalanceException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.TransactionDto;
import com.openclassrooms.PayMyBuddy.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class TransactionController {

    @Autowired
    private ITransactionService service;

    @Autowired
    private IUserService userService;

    @Autowired
    private IContactService contactService;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public static String TRANSACTION_SUCCESS_MESSAGE = "\u2714 \u2007 Le virement a été effectué !";

    @GetMapping("/transaction")
    public String transactions(Model model, @RequestParam(name="page", required=false) Optional<Integer> page) {
        addAttributesToModel(model, page.orElse(1));
        TransactionDto transactionDto = new TransactionDto();
        model.addAttribute("transactionDto", transactionDto);
        return "transactions";
    }

    @PostMapping("/transaction")
    public String saveNewTransaction(@Valid TransactionDto transactionDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.info("Cannot save transaction : invalid form");
            addAttributesToModel(model, 1);
            return "transactions";
        }
        try {
            service.saveTransaction(transactionDto);
            logger.info("Transaction saved");
            redirectAttributes.addFlashAttribute("message", TRANSACTION_SUCCESS_MESSAGE);
            return "redirect:/transaction";
        } catch (UserNotFoundException | InsufficientBalanceException e) {
            logger.info("Cannot save transaction : " + e.getMessage());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            addAttributesToModel(model, 1);
            return "transactions";
        }
    }

    private void addAttributesToModel(Model model, int pageNumber) {
        Iterable<TransactionDto> transactions = service.getAllPaginated(Math.max(0,pageNumber - 1));
        model.addAttribute("transactions", transactions);
        Iterable<ContactDto> contacts = contactService.getContacts();
        model.addAttribute("contacts", contacts);
    }
}
