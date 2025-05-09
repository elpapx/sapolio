package com.pichanga.application.controller;

import com.pichanga.application.dto.LoginRequest;
import com.pichanga.application.dto.LoginResponse;
import com.pichanga.application.exception.InvalidLoginCredentialsException;
import com.pichanga.application.service.AuthService;
import com.pichanga.application.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Component
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        validate(request);
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(InvalidLoginCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidLoginCredentialsException e) {
        log.error("Login credentials error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void validate(LoginRequest request) {
        if (request.getEmail() == null || !ValidatorUtil.isValidEmail(request.getEmail())) {
            throw new InvalidLoginCredentialsException("Email is not valid.");
        }
    }
}
