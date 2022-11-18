package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.ContactCannotBeCurrentUserException;
import com.openclassrooms.PayMyBuddy.exception.PayerRecipientAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ContactController {

    public static final String DELETE_CONTACT_SUCCESS_MESSAGE = "\u2714 \u2007 Le contact a été supprimé !";
    public static final String ADD_CONTACT_SUCCESS_MESSAGE = "\u2714 \u2007 Le contact a été ajouté !";
    @Autowired
    private IContactService service;

    @GetMapping("/contact")
    public String contacts(Model model) {
        Iterable<ContactDto> contacts = service.getContacts();
        model.addAttribute("contacts", contacts);
        EmailAddress emailAddress = new EmailAddress();
        model.addAttribute("emailAddress", emailAddress);
        return "contact";
    }

    @PostMapping("/contact")
    public String createContact(@Valid EmailAddress emailAddress, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            Iterable<ContactDto> contacts = service.getContacts();
            model.addAttribute("contacts", contacts);
            return "contact";
        }
        try {
            service.saveContact(emailAddress.getAddress());
            redirectAttributes.addFlashAttribute("message", ADD_CONTACT_SUCCESS_MESSAGE);
            return "redirect:/contact";
        } catch (UserNotFoundException | PayerRecipientAlreadyExistsException | ContactCannotBeCurrentUserException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            Iterable<ContactDto> contacts = service.getContacts();
            model.addAttribute("contacts", contacts);
            return "contact";
        }
    }

    @GetMapping("/deleteContact/{recipientId}")
    public ModelAndView deleteContact(@PathVariable("recipientId") final Long recipientId, RedirectAttributes redirectAttributes) {
        service.deleteContact(recipientId);
        redirectAttributes.addFlashAttribute("message", DELETE_CONTACT_SUCCESS_MESSAGE);
        return new ModelAndView("redirect:/contact");
    }
}
