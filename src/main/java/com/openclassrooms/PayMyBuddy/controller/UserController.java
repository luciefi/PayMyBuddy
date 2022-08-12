package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/contact")
    public String contacts(Model model) {
        Iterable<User> contacts = null;
                //contacts = service.getContacts();
        model.addAttribute("contacts", contacts);
        return "contact";
    }

    @GetMapping("/createContact")
    public String createContact(Model model) {
        String email = null;
        model.addAttribute("email", email);
        return "createBankAccount";
    }

    @PostMapping("/saveContact")
    public ModelAndView saveContact(@ModelAttribute String email) {
        boolean isContactCreationOk = service.saveContact(email);
        if(!isContactCreationOk){

        }
        return new ModelAndView("redirect:/contact");
    }
}
