package com.majlishekhidmat.service;

import com.majlishekhidmat.dto.UserDto;
import com.majlishekhidmat.entity.User;

import java.util.List;

public interface AdminUserService {
    List<User> getAllUsers();

    User getUserByEmail(String email);

    User updateUser(String email, UserDto userDto);

    void deleteUser(String email);

    void blockUser(String email, boolean block);
}
