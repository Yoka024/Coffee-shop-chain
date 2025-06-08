package com.offee.Coffee.shop.chain.controller;

import com.offee.Coffee.shop.chain.entity.User;
import com.offee.Coffee.shop.chain.service.UserService;
import com.offee.Coffee.shop.chain.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    //  реєстрація
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    //  логін (JSON + JWT)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        final UserDetails userDetails = userService.loadUserByUsername(user.getUsername());
        final String token = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(token);
    }

    // HTML логін з форми
    @PostMapping("/login-form")
    public String loginFromForm(@RequestParam String username,
                                @RequestParam String password,
                                Model model,
                                HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException ex) {
            model.addAttribute("error", "Невірний логін або пароль");
            return "login";
        }

        final UserDetails userDetails = userService.loadUserByUsername(username);
        final String token = jwtUtil.generateToken(userDetails.getUsername());

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60); // 1 день

        response.addCookie(jwtCookie);

        return "redirect:/";
    }

    // HTML реєстрація з форми
    @PostMapping("/register-form")
    public String registerFromForm(@RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam(required = false) String fullName,
                                   @RequestParam(required = false) String email,
                                   Model model) {

        if (userService.existsByUsername(username)) {
            model.addAttribute("error", "Користувач з таким ім'ям вже існує");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);

        userService.saveUser(user);

        // Після успішної реєстрації редіректимо на сторінку входу з параметром
        return "redirect:/login?registered=1";
    }

    //  форми логіну
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html
    }
}
