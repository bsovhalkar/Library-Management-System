package com.app.Library_Management.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Password Reset Token Request DTO
 * Captures email for sending password reset token
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenRequest {

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
}

