package com.example.springboot.controller;

import com.example.springboot.dto.LoginDTO;
import com.example.springboot.dto.PasswordResetDTO;
import com.example.springboot.dto.UserRegistrationDTO;
import com.example.springboot.model.User;
import com.example.springboot.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() throws Exception {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUsername("testuser");
        registrationDTO.setPassword("password");

        User user = new User();
        user.setUsername("testuser");

        when(authService.registerUser(any(UserRegistrationDTO.class))).thenReturn(user);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));

        verify(authService, times(1)).registerUser(any(UserRegistrationDTO.class));
    }

    @Test
    void testLogin_Success() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password");

        Map<String, Object> loginResponse = Map.of("token", "sample-token", "role", "USER");
        when(authService.loginWithRole(any(LoginDTO.class))).thenReturn(Optional.of(loginResponse));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("sample-token"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(authService, times(1)).loginWithRole(any(LoginDTO.class));
    }

    @Test
    void testLogin_Failure() throws Exception {
        when(authService.loginWithRole(any(LoginDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"wrongpassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));

        verify(authService, times(1)).loginWithRole(any(LoginDTO.class));
    }

    @Test
    void testResetPassword() throws Exception {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setUsername("testuser");
        passwordResetDTO.setNewPassword("newpassword");

        when(authService.resetPassword(any(PasswordResetDTO.class))).thenReturn("Password reset successfully");

        mockMvc.perform(post("/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"newPassword\": \"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));

        verify(authService, times(1)).resetPassword(any(PasswordResetDTO.class));
    }
}