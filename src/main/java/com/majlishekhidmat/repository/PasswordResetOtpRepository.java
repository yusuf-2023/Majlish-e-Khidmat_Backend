package com.majlishekhidmat.repository;

import com.majlishekhidmat.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM PasswordResetOtp o WHERE o.email = :email")
    void deleteByEmail(@Param("email") String email);

    Optional<PasswordResetOtp> findByEmail(String email);
}
