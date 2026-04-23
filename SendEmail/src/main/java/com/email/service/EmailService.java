package com.email.service;

import com.email.dto.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("raveendra@electems.com"); // Using configured username
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getBody());

        mailSender.send(message);
    }
    public void sendEmailWithAttachment(String to, String subject, String body, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = multipart

            helper.setFrom("raveendra@electems.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // false = plain text (true = HTML)

            // Attach file
            helper.addAttachment(file.getOriginalFilename(), file);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error while sending email with attachment", e);
        }
    }
}
