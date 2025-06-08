package com.offee.Coffee.shop.chain.config;

import com.offee.Coffee.shop.chain.filter.JwtFilter;
import com.offee.Coffee.shop.chain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserService userService;

    @Autowired
    public SecurityConfig(JwtFilter jwtFilter, UserService userService) {
        this.jwtFilter = jwtFilter;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/auth/**", "/login", "/register",
                                "/css/**", "/js/**", "/images/**",
                                "/oauth2/**", "/login/oauth2/**",
                                "/log-test" // <-- ось доданий відкритий маршрут
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Google OAuth2 login
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth/login") // спільна сторінка логіну
                        .defaultSuccessUrl("/", true) // після успіху — на головну
                )

                // Форма логіну
                .formLogin(form -> form
                        .loginPage("/auth/login")               // GET: форма логіну
                        .loginProcessingUrl("/auth/login-form") // POST: логін
                        .defaultSuccessUrl("/", true)           // після входу
                        .failureUrl("/auth/login?error=true")   // у разі помилки
                        .permitAll()
                )

                // Stateless (без сесій)
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Логаут
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .deleteCookies("jwt")
                        .permitAll()
                )

                // JWT-фільтр перед стандартною автентифікацією
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
