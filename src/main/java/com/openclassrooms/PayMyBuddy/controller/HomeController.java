package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.IncorrectCredentialsException;
import com.openclassrooms.PayMyBuddy.model.LoginDto;
import com.openclassrooms.PayMyBuddy.service.IUserService;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public String home(Model model) {
        if (CurrentUserUtils.getCurrentUserId() == null) {
            return "unauthenticatedHome";
        }
        double balance = userService.getBalance();
        model.addAttribute("balance", balance);
        return "home";
    }

    @GetMapping("/sitemap")
    public String sitemap(Model model) {
        return "sitemap";
    }

    @GetMapping("/login")
    public String login(Model model) {
        LoginDto loginDto = new LoginDto();
        model.addAttribute("loginDto", loginDto);
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid LoginDto loginDto, BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }
        try {
            userService.logUserIn(loginDto);
            return "redirect:/";
        } catch (IncorrectCredentialsException e) {
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "login";
        }
    }
}
