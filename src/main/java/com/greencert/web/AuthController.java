package com.greencert.web;

import com.greencert.db.dao.UserDAO;
import com.greencert.db.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserDAO userDAO;

    public AuthController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) { // In a real app, use BCrypt
            session.setAttribute("user", user);
            return "redirect:/";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, 
                           @RequestParam String companyName, Model model) {
        if (userDAO.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        User newUser = new User(username, password, companyName);
        userDAO.save(newUser);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
