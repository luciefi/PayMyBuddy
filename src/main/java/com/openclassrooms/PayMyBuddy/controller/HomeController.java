package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.service.UserService;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model) {
    if(CurrentUserUtils.getCurrentUserId() == null){
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
}
