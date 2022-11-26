package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.service.IUserService;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private IUserService userService;

    @GetMapping("/")
    public String home(Model model) {
        if (CurrentUserUtils.getCurrentUserId() == null) {
            return "unauthenticatedHome";
        }
        return getAuthenticatedHome(model);
    }

    @GetMapping("/sitemap")
    public String sitemap(Model model) {
        return "sitemap";
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (CurrentUserUtils.getCurrentUserId() == null) {
            return "login";
        }
        return getAuthenticatedHome(model);
    }

    protected String getAuthenticatedHome(Model model){
        double balance = userService.getBalance();
        model.addAttribute("balance", balance);
        return "home";
    }
}
