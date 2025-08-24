package com.majlishekhidmat.serviceimpl;

import com.majlishekhidmat.entity.Admin;
import com.majlishekhidmat.entity.PasswordResetOtp;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.repository.AdminRepository;
import com.majlishekhidmat.repository.PasswordResetOtpRepository;
import com.majlishekhidmat.repository.UserRepository;
import com.majlishekhidmat.service.EmailService;
import com.majlishekhidmat.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Step 1: Send OTP to email (for user or admin automatically)
    @Override
    @Transactional
    public void sendOtp(String email) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        Optional<Admin> optionalAdmin = adminRepository.findByEmailIgnoreCase(email);

        if (optionalUser.isEmpty() && optionalAdmin.isEmpty()) {
            throw new RuntimeException("No user or admin found with email: " + email);
        }

        // Generate 6-digit OTP
        String otp = "%06d".formatted(new Random().nextInt(1000000));

        // Remove existing OTP if any
        otpRepository.deleteByEmail(email);

        // Save new OTP with expiry 5 minutes
        PasswordResetOtp resetOtp = PasswordResetOtp.builder()
                .email(email)
                .otp(otp)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .build();
        otpRepository.save(resetOtp);

        // Get recipient name for email greeting
        String name = optionalUser.map(User::getName)
                .orElseGet(() -> optionalAdmin.map(Admin::getName).orElse("User"));

        // Prepare email
        String subject = "Majlis-e-Khidmat: OTP for Password Reset";
        String body = "Dear %s,\n\nYour OTP for password reset is: %s\n\nThis OTP is valid for 5 minutes.\n\nRegards,\nMajlis-e-Khidmat Team".formatted(
                name, otp
        );

        // Send OTP email
        emailService.sendEmail(email, subject, body);
    }

    // Step 2: Verify OTP and reset password (for user or admin)
    @Override
    @Transactional
    public void verifyOtpAndResetPassword(String email, String otp, String newPassword) {
        PasswordResetOtp resetOtp = otpRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP has not been requested for this email."));

        if (!resetOtp.getOtp().equals(otp)) {
            throw new RuntimeException("The OTP you entered is incorrect.");
        }

        if (resetOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            otpRepository.deleteByEmail(email);
            throw new RuntimeException("The OTP has expired. Please request a new one.");
        }

        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        Optional<Admin> optionalAdmin = adminRepository.findByEmailIgnoreCase(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        } else if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
        } else {
            throw new RuntimeException("No user or admin found for password reset.");
        }

        // Delete OTP after successful password reset
        otpRepository.deleteByEmail(email);
    }
}
