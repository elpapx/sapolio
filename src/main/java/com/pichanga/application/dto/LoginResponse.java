package com.pichanga.application.dto;

import lombok.*;


@Getter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String firstName;
    private String lastName;
}

