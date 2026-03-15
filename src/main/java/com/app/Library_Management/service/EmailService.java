package com.app.Library_Management.service;

import jakarta.validation.constraints.Email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
