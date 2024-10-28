package com.example.springboot.controller;

import com.example.springboot.model.Avatar;
import com.example.springboot.service.BaiduAvatarService;
import com.example.springboot.repository.AvatarRepository;
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

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvatarController.class)
@AutoConfigureMockMvc(addFilters = false) // 禁用 CSRF 保护
class AvatarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BaiduAvatarService baiduAvatarService;

    @MockBean
    private AvatarRepository avatarRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateTemporaryAvatar_Success() throws Exception {
        String keyword = "testKeyword";
        String imageUrl = "http://image-url.com/avatar.png";

        when(baiduAvatarService.generateTemporaryAvatar(keyword)).thenReturn(imageUrl);

        mockMvc.perform(post("/user/avatar/generate")
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(imageUrl));

        verify(baiduAvatarService, times(1)).generateTemporaryAvatar(keyword);
    }

    @Test
    void testGenerateTemporaryAvatar_Failure() throws Exception {
        String keyword = "testKeyword";

        when(baiduAvatarService.generateTemporaryAvatar(keyword)).thenThrow(new IOException("Failed to generate avatar"));

        mockMvc.perform(post("/user/avatar/generate")
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error generating avatar: Failed to generate avatar"));

        verify(baiduAvatarService, times(1)).generateTemporaryAvatar(keyword);
    }

    @Test
    void testSaveAvatar() throws Exception {
        Long userId = 1L;
        String imageUrl = "http://image-url.com/avatar.png";
        String keyword = "testKeyword";

        when(baiduAvatarService.saveAvatar(userId, imageUrl, keyword)).thenReturn(imageUrl);

        mockMvc.perform(post("/user/avatar/save")
                        .param("userId", userId.toString())
                        .param("imageUrl", imageUrl)
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Avatar saved successfully."));

        verify(baiduAvatarService, times(1)).saveAvatar(userId, imageUrl, keyword);
    }

    @Test
    void testViewAvatar_Success() throws Exception {
        Long userId = 1L;
        Avatar avatar = new Avatar();
        avatar.setUserId(userId);
        avatar.setAvatarData("http://image-url.com/avatar.png");

        when(baiduAvatarService.getAvatarByUserId(userId)).thenReturn(Optional.of(avatar));

        mockMvc.perform(get("/user/avatar/view/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.avatarData").value("http://image-url.com/avatar.png"));

        verify(baiduAvatarService, times(1)).getAvatarByUserId(userId);
    }

    @Test
    void testViewAvatar_NotFound() throws Exception {
        Long userId = 1L;

        when(baiduAvatarService.getAvatarByUserId(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/avatar/view/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Avatar not found for userId: " + userId));

        verify(baiduAvatarService, times(1)).getAvatarByUserId(userId);
    }

    @Test
    void testDeleteAvatar_Success() throws Exception {
        Long userId = 1L;

        when(baiduAvatarService.deleteAvatarByUserId(userId)).thenReturn(true);

        mockMvc.perform(delete("/user/avatar/delete/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Avatar deleted successfully for userId: " + userId));

        verify(baiduAvatarService, times(1)).deleteAvatarByUserId(userId);
    }

    @Test
    void testDeleteAvatar_NotFound() throws Exception {
        Long userId = 1L;

        when(baiduAvatarService.deleteAvatarByUserId(userId)).thenReturn(false);

        mockMvc.perform(delete("/user/avatar/delete/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Avatar not found for userId: " + userId));

        verify(baiduAvatarService, times(1)).deleteAvatarByUserId(userId);
    }
}