package com.pichanga.application.controller;

import com.pichanga.application.dto.RegisterRequest;
import com.pichanga.application.dto.RegisterResponse;
import com.pichanga.application.exception.RegistrationException;
import com.pichanga.application.exception.ValidationException;
import com.pichanga.application.service.RegistrationService;
import com.pichanga.application.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@RestController
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        validate(request);
        return registrationService.register(request);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        log.error("Validation error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException e) {
        log.error("Registration error: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    private void validate(RegisterRequest request) {
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new ValidationException("First name is required.");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new ValidationException("Last name is required.");
        }
        if (request.getEmail() == null || !ValidatorUtil.isValidEmail(request.getEmail())) {
            throw new ValidationException("A valid email is required.");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters.");
        }
    }
}
