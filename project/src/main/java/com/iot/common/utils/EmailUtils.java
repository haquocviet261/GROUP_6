package com.iot.common.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailWithInforNewAccount(String email, String username, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Your Account Details");

        mimeMessageHelper.setText("""
                <div>
                    Hello, %s
                    <br>
                    ** This is an automated message -- please do not reply as you will not receive a response. **
                    <br><br>
                    Your account has been created successfully.
                    <br>
                    <strong>Username:</strong> %s
                    <br>
                    <strong>Password:</strong> %s
                    <br><br>
                    Please log in and change your password after the first login.
                </div>
                """.formatted(username, username, password), true);

        javaMailSender.send(mimeMessage);
    }

    public void confirmAccount(String email,String token) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Account");
        mimeMessageHelper.setText("""
                <div>
                    Hello, %s
                    <br><br>
                    Thank you for registering with us! Please click the link below to verify your email address and activate your account:
                    <br><br>
                    <a href="http://localhost:9999/api/user/verify-account?token=%s" target="_blank">Click here to verify your account</a>
                    <br><br>
                    If you did not sign up for this account, please ignore this email.
                    <br><br>
                    Best regards,<br>
                    [Your Company Name] Team
                </div>
                """.formatted(email,token), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendEmailToResetPassword(String email, String userName, String newPassword) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Password Reset Request");
        mimeMessageHelper.setText("""
                <div>
                  <p>Hello, %s</p>
                  <p>** This is an automated message -- please do not reply as you will not receive a response. **</p>
                  <p>This message is in response to your request to reset your account password.</p>
                  <p>Your new password is: <strong>%s</strong></p>
                  <p>Please use this password to log in and make sure to change it after your first login.</p>
                </div>
                """.formatted(userName, newPassword), true);
        javaMailSender.send(mimeMessage);
    }
}
