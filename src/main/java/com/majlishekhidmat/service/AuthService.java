package com.majlishekhidmat.service;

import com.majlishekhidmat.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
