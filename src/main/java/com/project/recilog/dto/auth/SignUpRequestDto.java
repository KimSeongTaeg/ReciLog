package com.project.recilog.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String username;
}