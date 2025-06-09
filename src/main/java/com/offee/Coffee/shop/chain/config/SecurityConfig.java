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
                                "/h2-console/**", "/h2-console",
                                "/log-test"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // Дозволити framing для H2 консолі
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
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

                // Змішана стратегія: сесії для HTML форм, JWT для API
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Логаут
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .deleteCookies("jwt")
                        .permitAll()
                        .logoutRequestMatcher(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/logout", "GET"))
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
