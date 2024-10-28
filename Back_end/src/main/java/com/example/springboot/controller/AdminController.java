package com.example.springboot.controller;

import com.example.springboot.model.Admin;
import com.example.springboot.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Admin admin = adminService.createAdmin(username, password);
        return ResponseEntity.status(201).body(Map.of(
                "message", "admin created successfully",
                "admin_id", admin.getAdminId().toString()
        ));
    }

    @GetMapping("/{adminId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAdminById(@PathVariable Long adminId) {
        Optional<Admin> adminOptional = adminService.getAdminById(adminId);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return ResponseEntity.ok(Map.of(
                    "admin_id", admin.getAdminId().toString(),
                    "username", admin.getUser().getUsername()
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Admin not found"
            ));
        }
    }

    @PutMapping("/update/{adminId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateAdmin(@PathVariable Long adminId, @RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        try {
            adminService.updateAdmin(adminId, username, password);
            return ResponseEntity.ok(Map.of(
                    "message", "Admin updated successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId) {
        try {
            adminService.deleteAdmin(adminId);
            return ResponseEntity.ok(Map.of(
                    "message", "Admin deleted successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
}
