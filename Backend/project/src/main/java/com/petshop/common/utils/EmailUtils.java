package com.petshop.common.utils;

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
    public void sendEmail(String email, String jwt) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Account");
        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:9999/api/v1/user/verify-account?email=%s&jwt=%s" target="_blank">click link to verify account</a>
        </div>
        """.formatted(email, jwt), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendSetPasswordEmail(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:9999/api/v1/user/set-password?email=%s" target="_blank">click link to set password</a>
        </div>
        """.formatted(email), true);

        javaMailSender.send(mimeMessage);
    }
}
