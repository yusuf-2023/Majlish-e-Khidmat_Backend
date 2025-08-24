package com.majlishekhidmat.service;

public interface OtpService {
    void generateAndSendOtp(String email);
    boolean verifyOtp(String email, String otp);
    void resetPassword(String email, String newPassword);
}
