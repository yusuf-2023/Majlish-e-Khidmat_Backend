package com.majlishekhidmat.dto;

import lombok.Data;

@Data
public class VerifyOtpRequestDto {
    private String email;
    private String otp;
    private String newPassword;
  
}
