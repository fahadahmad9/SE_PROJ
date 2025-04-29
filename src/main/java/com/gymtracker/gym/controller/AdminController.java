package com.gymtracker.gym.controller;

import com.gymtracker.gym.model.Admin;
import com.gymtracker.gym.model.User;
import com.gymtracker.gym.service.AdminService;
import com.gymtracker.gym.service.DashboardService;
import com.gymtracker.gym.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private DashboardService dashboardService;

    private final AdminService adminService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/main")
    public String showAdminDashboard() {
        return "admin"; // This will load admin.html
    }

    // Show Admin Login Page
    @GetMapping("/login")
    public String showAdminLoginForm() {
        return "admin-login";
    }

    @GetMapping("/signup")
    public String showAdminSignupForm() {
        return "admin-signup";
    }

    // Handle Admin Login
    @PostMapping("/login")
    public String loginAdmin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Admin admin = adminService.authenticateAdmin(email, password);
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            model.addAttribute("error", "Email and Password are required.");
            return "admin-login";
        }
        if (admin != null) {
            session.setAttribute("loggedInAdmin", admin);
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "admin-login";
        }
    }

    // Handle Admin Sign Up
    @PostMapping("/signup")
    public String registerAdmin(@RequestParam String name, @RequestParam String email, @RequestParam String password, Model model) {
        try {
            adminService.registerAdmin(name, email, password);
            return "redirect:/admin/main";  // Correct redirect after successful sign-up
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Admin already exists.");
            return "admin-signup";  // Stay on sign-up page if the admin already exists
        }
    }

    // Admin Dashboard
    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("admin", admin);
        return "admin-dashboard";
    }

    // View Pending Registrations
    @GetMapping("/pending")
    public String viewPendingRegistrations(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        List<User> pendingUsers = userService.getPendingRegistrations();
        model.addAttribute("pendingUsers", pendingUsers);
        return "admin-pending";
    }

    // Approve User
    @PostMapping("/approve/{userId}")
    public String approveUser(@PathVariable Long userId) {
        userService.approveUser(userId);
        return "redirect:/admin/pending";
    }

    // Reject User
    @PostMapping("/reject/{userId}")
    public String rejectUser(@PathVariable Long userId, @RequestParam String reason) {
        userService.rejectUser(userId, reason);
        return "redirect:/admin/pending";
    }
    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model) {
        Map<String, Object> stats = dashboardService.getDashboardStats();
        model.addAttribute("stats", stats);
        return "admin-dashboard";
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}