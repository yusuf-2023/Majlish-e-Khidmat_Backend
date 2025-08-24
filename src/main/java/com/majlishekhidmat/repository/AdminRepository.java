package com.majlishekhidmat.repository;

import com.majlishekhidmat.entity.Admin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    
    boolean existsByEmail(String email); // ✅ Add this
  Optional<Admin> findByEmailIgnoreCase(String email);    // If you use it elsewhere
}



