package com.app.Library_Management.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Password Reset Request DTO
 * Captures token and new password for password reset
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {

    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "New password is required")
    private String newPassword;
}

