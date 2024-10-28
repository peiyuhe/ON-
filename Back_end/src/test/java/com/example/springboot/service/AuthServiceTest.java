package com.example.springboot.service;

import com.example.springboot.dto.LoginDTO;
import com.example.springboot.dto.PasswordResetDTO;
import com.example.springboot.dto.UserRegistrationDTO;
import com.example.springboot.model.User;
import com.example.springboot.repository.StudentRepository;
import com.example.springboot.repository.TeacherRepository;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("testuser");
        registrationDTO.setPassword("password");
        registrationDTO.setRole("STUDENT");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = authService.registerUser(registrationDTO);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPasswordHash());
        assertEquals(User.Role.STUDENT, user.getRole());

        verify(studentRepository, times(1)).save(any());
    }

    @Test
    void testLoginWithRole_Success() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("encodedPassword");
        user.setRole(User.Role.STUDENT);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "STUDENT")).thenReturn("jwtToken");

        Optional<Map<String, Object>> loginResponse = authService.loginWithRole(loginDTO);

        assertTrue(loginResponse.isPresent());
        assertEquals("jwtToken", loginResponse.get().get("token"));
    }

    @Test
    void testResetPassword_Success() {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setUsername("testuser");
        passwordResetDTO.setSecurityAnswer("answer");
        passwordResetDTO.setNewPassword("newpassword");
        passwordResetDTO.setConfirmPassword("newpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setSecurityAnswer("answer");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

        String response = authService.resetPassword(passwordResetDTO);

        assertEquals("Password reset successful", response);
    }

    @Test
    void testResetPassword_Failure() {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setUsername("testuser");
        passwordResetDTO.setSecurityAnswer("wrongAnswer");
        passwordResetDTO.setNewPassword("newpassword");
        passwordResetDTO.setConfirmPassword("newpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setSecurityAnswer("answer");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        String response = authService.resetPassword(passwordResetDTO);

        assertEquals("Incorrect security answer", response);
    }
}