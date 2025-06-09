package com.offee.Coffee.shop.chain.controller;

import com.offee.Coffee.shop.chain.dto.*;
import com.offee.Coffee.shop.chain.entity.User;
import com.offee.Coffee.shop.chain.service.UserService;

import com.offee.Coffee.shop.chain.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());

            userService.saveUser(user);
            return ResponseEntity.ok(ApiResponse.success("User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }

    //  логін (JSON + JWT)
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(ApiResponse.error("Invalid username or password"));
        }

        final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        final String token = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(new AuthResponse(token)));
    }

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
        jwtCookie.setMaxAge(24 * 60 * 60);

        response.addCookie(jwtCookie);

        return "redirect:/";
    }

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

        return "redirect:/auth/login?registered=1";
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String registered,
                                @RequestParam(required = false) String error,
                                Model model) {
        if ("1".equals(registered)) {
            model.addAttribute("success", "Реєстрація успішна! Тепер ви можете увійти в систему");
        }
        if ("true".equals(error)) {
            model.addAttribute("error", "Невірний логін або пароль");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }
}
