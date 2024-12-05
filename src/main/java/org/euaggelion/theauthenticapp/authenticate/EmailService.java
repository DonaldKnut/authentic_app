package org.euaggelion.theauthenticapp.authenticate;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationOtpEmail(String email, String otp) throws MessagingException;
}
