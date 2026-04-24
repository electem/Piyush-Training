package com.emailopt.controller;

import com.emailopt.service.OtpService;
import com.emailopt.utility.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class EmailOtpController {

    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    public EmailOtpController(OtpService otpService, JwtUtil jwtUtil) {
        this.otpService = otpService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        otpService.sendOtp(email);
        return "OTP sent successfully";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email,
                            @RequestParam String otp) {

        boolean valid = otpService.validateOtp(email, otp);

        if (valid) {
            return jwtUtil.generateToken(email);
        }

        return "Invalid or expired OTP";
    }
}