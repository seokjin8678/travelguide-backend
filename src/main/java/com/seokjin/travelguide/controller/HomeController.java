package com.seokjin.travelguide.controller;

import com.seokjin.travelguide.dto.response.Response;
import com.seokjin.travelguide.dto.response.common.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok()
                .body("Hello World!");
    }

    @PostMapping("/api/v1/test")
    public ResponseEntity<Response> test() {
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .code("200")
                        .message("Hello World!")
                        .build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/api/v1/adminTest")
    public ResponseEntity<Response> adminTest() {
        return ResponseEntity.ok()
                .body(SuccessResponse.builder()
                        .code("200")
                        .message("Admin Test")
                        .build());
    }
}
