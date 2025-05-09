package com.pichanga.application.dto;

import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Builder
public class RegisterResponse {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final boolean isSuccessful;
}
