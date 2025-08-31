package com.majlishekhidmat.serviceimpl;

import com.majlishekhidmat.dto.UserDto;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.repository.AdminUserRepository;
import com.majlishekhidmat.service.AdminUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // ✅ Yeh import zaroori hai

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User updateUser(String email, UserDto userDto) {
        User user = getUserByEmail(email);

        user.setName(userDto.getName());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        user.setGender(userDto.getGender());

        try {
            if (userDto.getDob() != null && !userDto.getDob().isEmpty()) {
                LocalDate dob = LocalDate.parse(userDto.getDob(), DateTimeFormatter.ISO_DATE);
                user.setDob(dob);
            } else {
                user.setDob(null);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid DOB format");
        }

        if (userDto.getProfilePic() != null) {
            user.setProfilePic(userDto.getProfilePic());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional // ✅ Yahi woh mukhya badlaav hai
    public void deleteUser(String email) {
        User user = getUserByEmail(email);
        userRepository.delete(user);
    }

    @Override
    public void blockUser(String email, boolean block) {
        User user = getUserByEmail(email);
        user.setBlocked(block);
        userRepository.save(user);
    }
}