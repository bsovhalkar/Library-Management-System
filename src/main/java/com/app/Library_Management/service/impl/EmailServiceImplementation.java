package com.app.Library_Management.service.impl;

import com.app.Library_Management.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService {
    private final JavaMailSender mailSender;
    @Override
    public void sendEmail(String to, String subject, String body) {

        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setSubject(subject);
            helper.setTo(to);
            helper.setText(body, true);
            mailSender.send(mimeMessage);
        }
        catch (MailException ex){
            throw new MailSendException("Failed to send email to " + to, ex);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } finally{}

    }
}
