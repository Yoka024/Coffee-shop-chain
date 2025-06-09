package com.offee.Coffee.shop.chain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        // тримуємо інформацію про авторизацію
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            //  користувач авторизований — додаємо його ім'я
            String username = authentication.getName();
            model.addAttribute("username", username);
        }

        return "index";
    }
}
