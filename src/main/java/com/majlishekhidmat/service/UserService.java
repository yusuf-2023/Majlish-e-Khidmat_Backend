package com.majlishekhidmat.service;

import com.majlishekhidmat.dto.UserDto;
import com.majlishekhidmat.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User registerUser(UserDto dto);
    User getUserByEmail(String email);
    User updateUser(String email, UserDto dto);
    void deleteUser(String email);
    
    User getUserById(Long id);
    void deleteUserById(Long id);
    User uploadProfilePic(String email, MultipartFile file);
}
