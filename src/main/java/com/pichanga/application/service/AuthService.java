package com.pichanga.application.service;

import com.pichanga.application.dto.LoginRequest;
import com.pichanga.application.dto.LoginResponse;
import com.pichanga.application.entity.sql.AppUser;
import com.pichanga.application.exception.InvalidLoginCredentialsException;
import com.pichanga.application.repository.sql.AppUserRepository;
import com.pichanga.application.repository.sql.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginResponse login(LoginRequest request) {
        AppUser appUser = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (appUser == null || !bCryptPasswordEncoder.matches(request.getPassword(), appUser.getPassword())) {
            throw new InvalidLoginCredentialsException("Invalid email or password");
        }

        if (!appUser.isEnabled()) {
            throw new InvalidLoginCredentialsException("User not enabled. Please confirm your email.");
        }

        return LoginResponse.builder()
                .email(appUser.getEmail())
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .build();
    }
}
