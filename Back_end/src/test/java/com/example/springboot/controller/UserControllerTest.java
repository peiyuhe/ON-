package com.example.springboot.controller;

import com.example.springboot.dto.SecuritySetupDTO;
import com.example.springboot.model.User;
import com.example.springboot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testCheckSecurityQuestion_Set() throws Exception {
        when(userService.hasSetSecurityQuestion(1L)).thenReturn(true);

        mockMvc.perform(get("/user/check-security/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Security question is already set."));
    }

    @Test
    void testCheckSecurityQuestion_NotSet() throws Exception {
        when(userService.hasSetSecurityQuestion(1L)).thenReturn(false);

        mockMvc.perform(get("/user/check-security/1"))
                .andExpect(status().isPartialContent())
                .andExpect(content().string("Please set your security question."));
    }

    @Test
    void testSetSecurityQuestion() throws Exception {
        SecuritySetupDTO securitySetupDTO = new SecuritySetupDTO();
        when(userService.setSecurityQuestion(any(SecuritySetupDTO.class))).thenReturn("Security question set successfully");

        mockMvc.perform(post("/user/set-security-question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"What is your pet's name?\",\"answer\":\"Fluffy\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Security question set successfully"));
    }

    @Test
    void testUpdateSecurityQuestion() throws Exception {
        SecuritySetupDTO securitySetupDTO = new SecuritySetupDTO();
        when(userService.updateSecurityQuestion(eq(1L), any(SecuritySetupDTO.class))).thenReturn("Security question updated successfully");

        mockMvc.perform(put("/user/update-security-question/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"What is your mother's maiden name?\",\"answer\":\"Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Security question updated successfully"));
    }

    @Test
    void testDeleteSecurityQuestion() throws Exception {
        when(userService.deleteSecurityQuestion(1L)).thenReturn("Security question deleted successfully");

        mockMvc.perform(delete("/user/delete-security-question/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Security question deleted successfully"));
    }

    @Test
    void testUploadAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.jpg", "image/jpeg", "test image content".getBytes());
        when(userService.uploadAvatar(eq(1L), any(MultipartFile.class))).thenReturn("http://image-url.com/avatar.jpg");

        mockMvc.perform(multipart("/user/upload-avatar/1").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Avatar uploaded successfully: http://image-url.com/avatar.jpg"));
    }

    @Test
    void testUpdateAvatar() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "avatar.jpg", "image/jpeg", "test image content".getBytes());
        when(userService.uploadAvatar(eq(1L), any(MultipartFile.class))).thenReturn("http://image-url.com/avatar.jpg");

        mockMvc.perform(multipart("/user/update-avatar/1").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Avatar updated successfully: http://image-url.com/avatar.jpg"));
    }

    @Test
    void testDeleteAvatar() throws Exception {
        when(userService.deleteAvatar(1L)).thenReturn("Avatar deleted successfully");

        mockMvc.perform(delete("/user/delete-avatar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Avatar deleted successfully"));
    }

    @Test
    void testGetAvatar() throws Exception {
        when(userService.getAvatar(1L)).thenReturn("http://image-url.com/avatar.jpg");

        mockMvc.perform(get("/user/get-avatar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("http://image-url.com/avatar.jpg"));
    }

    @Test
    void testGetUserByUsername_Found() throws Exception {
        User user = new User();
        user.setUsername("johndoe");
        when(userService.getUserByUsername("johndoe")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user/johndoe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    void testGetUserByUsername_NotFound() throws Exception {
        when(userService.getUserByUsername("johndoe")).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/johndoe"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error: User not found"));
    }
}