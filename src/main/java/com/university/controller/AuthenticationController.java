package com.university.controller;

import com.university.dto.AuthenticationRequest;
import com.university.dto.AuthenticationResponse;
import com.university.dto.RegisterRequest;
import com.university.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(service.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest authenticationRequest) {
        System.out.println(authenticationRequest);
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }
}
