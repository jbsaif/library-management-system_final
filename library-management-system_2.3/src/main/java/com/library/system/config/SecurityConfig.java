package com.library.system.config;

import com.library.system.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider; // NEW IMPORT
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager; // NEW IMPORT
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {

    // 1. Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Custom UserDetailsService Bean (Database)
    @Bean
    public UserDetailsService userDetailsService(CustomUserDetailsService customUserDetailsService) {
        return customUserDetailsService;
    }

    // 3. In-Memory Authentication Provider (for Admin fallback)
    @Bean
    public AuthenticationProvider inMemoryAuthenticationProvider(PasswordEncoder pw) {
        var admin = User.withUsername("admin")
                .password(pw.encode("admin123"))
                .roles("ADMIN")
                .build();

        // This manager only holds the hardcoded admin account
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(admin);

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(manager);
        authProvider.setPasswordEncoder(pw);
        return authProvider;
    }

    // 4. Database Authentication Provider (for registered users)
    @Bean
    public AuthenticationProvider databaseAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // 5. Filter Chain - Now registers both providers
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationProvider inMemoryAuthenticationProvider, // Inject admin provider
                                           AuthenticationProvider databaseAuthenticationProvider) throws Exception { // Inject user provider

        // Register both providers with Spring Security
        http.authenticationProvider(inMemoryAuthenticationProvider)
                .authenticationProvider(databaseAuthenticationProvider);

        http
                .authorizeHttpRequests(auth -> auth
                        // Public Paths: Login, register, static resources, books list, h2-console
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/", "/books", "/books/category/**", "/h2-console/**").permitAll()

                        // User Paths: Borrowing books and viewing their borrowed list
                        .requestMatchers("/books/borrow/**", "/borrowed/**").hasRole("USER")

                        // Admin Paths: Add/Edit books and any other admin route
                        .requestMatchers("/admin/**", "/books/add", "/books/edit/**").hasRole("ADMIN")

                        // All other requests must be authenticated (as a fallback)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/books", true)
                )
                .logout(logout -> logout.permitAll())

                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }
}