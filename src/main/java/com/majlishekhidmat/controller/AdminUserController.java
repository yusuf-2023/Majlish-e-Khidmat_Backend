package com.majlishekhidmat.controller;

import com.majlishekhidmat.dto.UserDto;
import com.majlishekhidmat.entity.User;
import com.majlishekhidmat.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // your frontend URLs
public class AdminUserController {

    private final AdminUserService adminUserService;

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get user by email
    @GetMapping("/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = adminUserService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // Update user by email
    @PutMapping("/update/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody UserDto userDto) {
        User updatedUser = adminUserService.updateUser(email, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user by email
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        adminUserService.deleteUser(email);
        return ResponseEntity.ok("User deleted successfully");
    }

    // Block or unblock user
    @PutMapping("/block/{email}")
    public ResponseEntity<String> blockUser(@PathVariable String email, @RequestParam boolean block) {
        adminUserService.blockUser(email, block);
        String msg = block ? "User blocked successfully" : "User unblocked successfully";
        return ResponseEntity.ok(msg);
    }
}
