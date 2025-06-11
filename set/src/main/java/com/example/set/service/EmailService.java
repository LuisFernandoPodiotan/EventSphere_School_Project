package com.example.set.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Send password reset email
    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("eventsphere07@gmail.com"); 
        helper.setTo(to);
        helper.setSubject("Password Reset Request");

        String content = """
            <html>
                <body>
                    <h2>Reset Your Password</h2>
                    <p>We received a request to reset your password. Click the link below to reset it:</p>
                    <a href="http://localhost:8080/user/reset-password?token=%s">Reset Password</a>
                    <p>This link will expire in 1 hour.</p>
                </body>
            </html>
        """.formatted(token);

        helper.setText(content, true);
        mailSender.send(message);
    }
    
    
    public void sendVerificationEmail(String to, String token) {
        String subject = "Email Verification";
        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        String body = "<p>Click the link below to verify your email:</p>"
                + "<a href=\"" + verificationUrl + "\">Verify Email</a>";

        sendEmail(to, subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // Enable HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
