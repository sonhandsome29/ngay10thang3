package com.example.sondeptraidemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping({"/order", "/oder"})
    public String order(Authentication authentication) {
        return "Order page for " + authentication.getName();
    }
}
