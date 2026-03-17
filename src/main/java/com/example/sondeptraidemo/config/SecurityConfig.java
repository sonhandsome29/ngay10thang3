package com.example.sondeptraidemo.config;

import com.example.sondeptraidemo.service.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccountService accountService;

    public SecurityConfig(AccountService accountService) {
        this.accountService = accountService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error", "/login", "/access-denied", "/h2-console/**").permitAll()
                        .requestMatchers("/products/add", "/products/create", "/products/save").hasRole("ADMIN")
                        .requestMatchers("/products/edit", "/products/edit/**", "/products/update/**").hasRole("ADMIN")
                        .requestMatchers("/products/delete", "/products/delete/**").hasRole("ADMIN")
                        .requestMatchers("/order", "/order/**", "/oder", "/oder/**").hasRole("USER")
                        .requestMatchers("/products", "/products/", "/products/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .userDetailsService(accountService)
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout.permitAll())
                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}
