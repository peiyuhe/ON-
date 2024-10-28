package com.example.springboot.service;

import com.example.springboot.model.Admin;
import com.example.springboot.model.User;
import com.example.springboot.repository.AdminRepository;
import com.example.springboot.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Admin createAdmin(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(User.Role.ADMIN);

        Admin admin = new Admin();
        admin.setUser(user);

        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdminById(Long adminId) {
        return adminRepository.findById(adminId);
    }

    public void updateAdmin(Long adminId, String username, String password) {
        Optional<Admin> adminOptional = adminRepository.findById(adminId);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            if (username != null) {
                admin.getUser().setUsername(username);
            }
            if (password != null) {
                admin.getUser().setPasswordHash(passwordEncoder.encode(password));
            }
            adminRepository.save(admin);
        } else {
            throw new RuntimeException("admin not found");
        }
    }

    public void deleteAdmin(Long adminId) {
        if (adminRepository.existsById(adminId)) {
            adminRepository.deleteById(adminId);
        } else {
            throw new RuntimeException("admin not found");
        }
    }
}
