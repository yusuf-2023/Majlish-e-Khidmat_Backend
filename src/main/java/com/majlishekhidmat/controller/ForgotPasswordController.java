package com.majlishekhidmat.controller;

import com.majlishekhidmat.dto.ForgotPasswordRequestDto;
import com.majlishekhidmat.dto.VerifyOtpRequestDto;
import com.majlishekhidmat.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody ForgotPasswordRequestDto request) {
        // Email validation optional - add if needed
        forgotPasswordService.sendOtp(request.getEmail());
        return ResponseEntity.ok("OTP sent successfully to email");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequestDto request) {
        forgotPasswordService.verifyOtpAndResetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );
        return ResponseEntity.ok("Password reset successfully");
    }
}
