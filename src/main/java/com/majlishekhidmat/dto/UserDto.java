package com.majlishekhidmat.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String dob; // String format "YYYY-MM-DD"
    private String gender;
    private String profilePic;
    private boolean blocked;  // add this for blocking users
}