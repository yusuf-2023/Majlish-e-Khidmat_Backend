package com.majlishekhidmat.serviceimpl;

import com.majlishekhidmat.dto.UserDto;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.repository.UserRepository;
import com.majlishekhidmat.service.ProfilePicUploadService;
import com.majlishekhidmat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfilePicUploadService profilePicUploadService;

    @Override
    public User registerUser(UserDto dto) {
        if (userRepository.findByEmailIgnoreCase(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword().trim()))
                .role("USER") 
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .dob(dto.getDob() != null && !dto.getDob().isEmpty() ? LocalDate.parse(dto.getDob()) : null)
                .gender(dto.getGender())
                .profilePic(dto.getProfilePic())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElse(null);
    }

    @Override
    public User updateUser(String email, UserDto dto) {
        User existingUser = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        existingUser.setName(dto.getName());
        existingUser.setPhone(dto.getPhone());
        existingUser.setAddress(dto.getAddress());
        existingUser.setDob(dto.getDob() != null && !dto.getDob().isEmpty() ? LocalDate.parse(dto.getDob()) : null);
        existingUser.setGender(dto.getGender());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(dto.getPassword().trim()));
        }
        if (dto.getProfilePic() != null) {
            existingUser.setProfilePic(dto.getProfilePic());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        userRepository.delete(user);
    }

    @Override
    public User uploadProfilePic(String email, MultipartFile file) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        try {
            String filePath = profilePicUploadService.saveProfilePic(file);
            user.setProfilePic(filePath);
            return userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
