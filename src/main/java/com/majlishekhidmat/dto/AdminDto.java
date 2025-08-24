package com.majlishekhidmat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    private String phone;
    private String address;
    private LocalDate dob;
    private String gender;
    private String profilePic;

}
