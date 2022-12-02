package com.seokjin.travelguide.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @PostMapping("/signin")
    public String signin() {
        return "signin";
    }

    @PostMapping("/signup")
    public String signup() {
        return "signup";
    }
}
