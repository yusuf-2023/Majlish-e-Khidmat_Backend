package com.majlishekhidmat.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.majlishekhidmat.dto.AdminDto;
import com.majlishekhidmat.entity.Admin;

public interface AdminService {
    Admin registerAdmin(AdminDto adminDto, MultipartFile file); // ✅ Method signature changed
    List<Admin> getAllAdmins();
    String deleteAdmin(Long id);
    Admin updateAdmin(Long id, AdminDto adminDto);

    Admin getAdminByEmail(String email);
    Admin updateProfilePic(String email, MultipartFile file);
}
