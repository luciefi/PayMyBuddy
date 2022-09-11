package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.ContactCannotBeCurrentUserException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.EmailAddress;
import com.openclassrooms.PayMyBuddy.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ContactController {

    @Autowired
    private IContactService service;

    @GetMapping("/contact")
    public String contacts(Model model) {
        Iterable<ContactDto> contacts = service.getContacts();
        model.addAttribute("contacts", contacts);
        return "contact";
    }

    @GetMapping("/createContact")
    public String createContact(Model model) {
        EmailAddress emailAddress = new EmailAddress();
        model.addAttribute("emailAddress", emailAddress);
        return "createContact";
    }


    @PostMapping("/createContact")
    public String createNewContact(@Valid EmailAddress emailAddress, BindingResult result) {
        if (result.hasErrors()) {
            return "createContact";
        }
        try {
            service.saveContact(emailAddress.getAddress());
            return "redirect:/contact";
        } catch (UserNotFoundException | PayerRecipientAlreadyExistsException | ContactCannotBeCurrentUserException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "createContact";
        }
    }

    @GetMapping("/deleteContact/{email}")
    public ModelAndView deleteContact(@PathVariable("email") final String email) {
        service.deleteContact(email);
        return new ModelAndView("redirect:/contact");
    }

}
