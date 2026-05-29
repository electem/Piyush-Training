package com.emailopt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        try {
            System.out.println(" Sending OTP to: " + toEmail);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("raveendra@electems.com");
            message.setTo(toEmail);
            message.setSubject("Your OTP Code");
            message.setText(
                    "Your OTP is: " + otp + "\n\nValid for 5 minutes.\nDo not share it."
            );

            mailSender.send(message);

            System.out.println(" Email sent successfully");

        } catch (Exception e) {
            System.out.println(" Email failed: " + e.getMessage());
            throw new RuntimeException("Email sending failed");
        }
    }
}