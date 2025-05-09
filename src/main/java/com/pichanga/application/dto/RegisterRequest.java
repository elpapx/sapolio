package com.pichanga.application.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class RegisterRequest {
    private final String firstName;

    private final String lastName;

    private final String email;

    private final String password;
}
