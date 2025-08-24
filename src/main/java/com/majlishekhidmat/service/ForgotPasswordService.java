package com.majlishekhidmat.service;

public interface ForgotPasswordService {
    void sendOtp(String email);
    void verifyOtpAndResetPassword(String email, String otp, String newPassword);
}
