package com.pichanga.application.service;

import com.pichanga.application.dto.RegisterRequest;
import com.pichanga.application.dto.RegisterResponse;
import com.pichanga.application.entity.sql.AppUser;
import com.pichanga.application.entity.sql.ConfirmationToken;
import com.pichanga.application.exception.ValidationException;
import com.pichanga.application.repository.sql.AppUserRepository;
import com.pichanga.application.repository.sql.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class RegistrationService {
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    public RegisterResponse register(RegisterRequest request) {
        boolean userExists = appUserRepository.findByEmail(request.getEmail()).isPresent();
        if (userExists) {
            throw new ValidationException("Email is already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());
        AppUser appUser = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();
        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationToken.builder()
                .token(token)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .appUser(appUser)
                .build();
        confirmationTokenRepository.save(confirmationToken);

        emailService.send(appUser.getFirstName(), appUser.getEmail(), token);

        return RegisterResponse.builder()
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .email(appUser.getLastName())
                .isSuccessful(true)
                .build();
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Email is already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token has expired.");
        }

        int confirmedAt = confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
        log.info("Token confirmed at: {}", confirmedAt);

        appUserRepository.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "Token has been confirmed.";
    }

}
