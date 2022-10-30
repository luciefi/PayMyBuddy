package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.AlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.BankAccountAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.EmailAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.model.BankAccount;
import com.openclassrooms.PayMyBuddy.model.User;
import com.openclassrooms.PayMyBuddy.service.BankAccountService;
import com.openclassrooms.PayMyBuddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    public static final String NEW_PROFILE_SUCCESS_MESSAGE = "\u2714 Votre profil a été crée !";
    public static final String UPDATE_PROFILE_SUCCESS_MESSAGE = "\u2714 Votre profil a été mis à jour !";

    @GetMapping("/createProfile")
    public String createProfile(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "createProfile";
    }

    @PostMapping("/createProfile")
    public String saveNewProfile(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "createProfile";
        }
        try {
            service.saveUser(user);
            redirectAttributes.addFlashAttribute("message", NEW_PROFILE_SUCCESS_MESSAGE);
            return "redirect:/"; // TODO où rediriger + affichage du message
        } catch (EmailAlreadyExistsException e) { // TODO
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "createProfile";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = service.getCurrentUser();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "profile";
        }
        try {
            service.saveUser(user);
            redirectAttributes.addFlashAttribute("message", UPDATE_PROFILE_SUCCESS_MESSAGE);
            return "redirect:/profile";
        } catch (EmailAlreadyExistsException e) { // TODO
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "profile";
        }
    }
}
