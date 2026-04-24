package com.emailopt.service;

import com.emailopt.entity.EmailOtp;
import com.emailopt.repository.EmailOtpRepository;
import com.emailopt.utility.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private EmailOtpRepository repository;

    @Autowired
    private EmailService emailService;

    public void sendOtp(String email) {
        String otp = OtpUtil.generateOTP();

        //  Send email FIRST
        emailService.sendOtp(email, otp);

        // Save only if email success
        EmailOtp entity = new EmailOtp();
        entity.setEmail(email);
        entity.setOtp(otp);
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        entity.setUsed(false);

        repository.save(entity);

        System.out.println("OTP saved in DB");
    }

    public boolean validateOtp(String email, String otp) {
        EmailOtp record = repository.findTopByEmailOrderByIdDesc(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (record.isUsed()) {
            System.out.println(" OTP already used");
            return false;
        }

        if (record.getExpiryTime().isBefore(LocalDateTime.now())) {
            System.out.println(" OTP expired");
            return false;
        }

        if (record.getOtp().equals(otp)) {
            record.setUsed(true);
            repository.save(record);
            System.out.println(" OTP verified");
            return true;
        }

        System.out.println(" Invalid OTP");
        return false;
    }
}