package com.offee.Coffee.shop.chain.controller;

import com.offee.Coffee.shop.chain.entity.Cafe;
import com.offee.Coffee.shop.chain.entity.MenuItem;
import com.offee.Coffee.shop.chain.repository.CafeRepository;
import com.offee.Coffee.shop.chain.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    private final CafeRepository cafeRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public ViewController(CafeRepository cafeRepository, MenuItemRepository menuItemRepository) {
        this.cafeRepository = cafeRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        boolean isAuthenticated = userDetails != null;

        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("username", isAuthenticated ? userDetails.getUsername() : "Гість");

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Cafe> cafes = cafeRepository.findAll();
        List<MenuItem> menuItems = menuItemRepository.findAll();

        model.addAttribute("cafes", cafes);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("username", userDetails != null ? userDetails.getUsername() : "Користувач");

        return "dashboard";
    }
}
