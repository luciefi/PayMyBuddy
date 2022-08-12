package com.openclassrooms.PayMyBuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteMapController {

    @GetMapping("/sitemap")
    public String sitemap(Model model) {
        return "sitemap";
    }
}
