package com.example.springboot.service;

import com.example.springboot.model.Admin;
import com.example.springboot.model.User;
import com.example.springboot.repository.AdminRepository;
import com.example.springboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAdmin_ShouldReturnSavedAdmin() {
        // Arrange
        String username = "admin";
        String password = "password";
        String encodedPassword = "encodedPassword";
        User user = new User();
        Admin admin = new Admin();
        admin.setUser(user);

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        // Act
        Admin result = adminService.createAdmin(username, password);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(adminRepository, times(1)).save(any(Admin.class));
        assertEquals(encodedPassword, result.getUser().getPasswordHash());
        assertEquals(User.Role.ADMIN, result.getUser().getRole());
    }

    @Test
    void getAdminById_ShouldReturnAdmin_WhenAdminExists() {
        // Arrange
        Long adminId = 1L;
        Admin admin = new Admin();
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));

        // Act
        Optional<Admin> result = adminService.getAdminById(adminId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(admin, result.get());
    }

    @Test
    void getAdminById_ShouldReturnEmptyOptional_WhenAdminDoesNotExist() {
        // Arrange
        Long adminId = 1L;
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act
        Optional<Admin> result = adminService.getAdminById(adminId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void updateAdmin_ShouldUpdateAdmin_WhenAdminExists() {
        // Arrange
        Long adminId = 1L;
        String newUsername = "newAdmin";
        String newPassword = "newPassword";
        String encodedPassword = "encodedNewPassword";

        Admin admin = new Admin();
        User user = new User();
        admin.setUser(user);

        when(adminRepository.findById(adminId)).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        // Act
        adminService.updateAdmin(adminId, newUsername, newPassword);

        // Assert
        assertEquals(newUsername, user.getUsername());
        assertEquals(encodedPassword, user.getPasswordHash());
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void updateAdmin_ShouldThrowException_WhenAdminDoesNotExist() {
        // Arrange
        Long adminId = 1L;
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminService.updateAdmin(adminId, "username", "password"));
        assertEquals("admin not found", exception.getMessage());
    }

    @Test
    void deleteAdmin_ShouldDeleteAdmin_WhenAdminExists() {
        // Arrange
        Long adminId = 1L;
        when(adminRepository.existsById(adminId)).thenReturn(true);

        // Act
        adminService.deleteAdmin(adminId);

        // Assert
        verify(adminRepository, times(1)).deleteById(adminId);
    }

    @Test
    void deleteAdmin_ShouldThrowException_WhenAdminDoesNotExist() {
        // Arrange
        Long adminId = 1L;
        when(adminRepository.existsById(adminId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminService.deleteAdmin(adminId));
        assertEquals("admin not found", exception.getMessage());
    }
}