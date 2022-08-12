package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.ContactAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.ContactDto;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/contact")
    public String contacts(Model model) {
        Iterable<ContactDto> contacts = service.getContacts();
        model.addAttribute("contacts", contacts);
        return "contact";
    }

    @GetMapping("/createContact")
    public String createContact(Model model) {
        ContactDto contactDto = new ContactDto();
        model.addAttribute("contactDto", contactDto);
        return "createContact";
    }


    @PostMapping("/createContact")
    public String saveNewContact(@Valid ContactDto contactDto, BindingResult result) {
        if (result.hasErrors()) {
            return "createContact";
        }
        try {
            service.saveContact(contactDto.getEmail());
            return "redirect:/contact";
        } catch (ContactAlreadyExistsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "createContact";
        }
    }

    @PostMapping("/saveContact")
    public ModelAndView saveContact(@ModelAttribute String email) {
        boolean isContactCreationOk = service.saveContact(email);
        if(!isContactCreationOk){

        }
        return new ModelAndView("redirect:/contact");
    }
}
