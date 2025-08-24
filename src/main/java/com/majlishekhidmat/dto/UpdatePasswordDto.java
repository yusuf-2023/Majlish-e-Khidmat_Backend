package com.majlishekhidmat.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
    private String email;
    private String newPassword;
}
