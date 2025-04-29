package com.gymtracker.gym.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";  // loads index.html
    }

    @GetMapping("/admin-page")
    public String showAdminLoginPage() {
        return "admin"; // fixed to match actual login page
    }

    @GetMapping("/user-page")
    public String showUserLoginPage() {
        return "user"; // fixed to match actual login page
    }

}
