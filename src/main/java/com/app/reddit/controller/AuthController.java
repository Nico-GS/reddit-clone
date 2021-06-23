package com.app.reddit.controller;

import com.app.reddit.dto.AuthResponse;
import com.app.reddit.dto.LoginRequest;
import com.app.reddit.dto.RegisterRequest;
import com.app.reddit.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.activation.ActivationException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest) throws ActivationException {
        authService.register(registerRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity verify(@PathVariable String token) {
        authService.verifyToken(token);
        return new ResponseEntity<>("Account Activated", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthResponse register(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}