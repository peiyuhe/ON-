package com.example.springboot.service;

import com.example.springboot.dto.SecuritySetupDTO;
import com.example.springboot.model.User;
import com.example.springboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHasSetSecurityQuestion_whenUserHasSetSecurityQuestion() {
        User user = new User();
        user.setSecurityQuestion("What is your favorite color?");
        user.setSecurityAnswer("Blue");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertTrue(userService.hasSetSecurityQuestion(1L));
    }

    @Test
    void testHasSetSecurityQuestion_whenUserHasNotSetSecurityQuestion() {
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertFalse(userService.hasSetSecurityQuestion(1L));
    }

    @Test
    void testSetSecurityQuestion_whenUserExists() {
        User user = new User();
        user.setUsername("testuser");

        SecuritySetupDTO securitySetupDTO = new SecuritySetupDTO();
        securitySetupDTO.setUsername("testuser");
        securitySetupDTO.setSecurityQuestion("What is your pet's name?");
        securitySetupDTO.setSecurityAnswer("Buddy");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        String result = userService.setSecurityQuestion(securitySetupDTO);
        assertEquals("Security question set successfully", result);
        verify(userRepository).save(user);
    }

    @Test
    void testSetSecurityQuestion_whenUserDoesNotExist() {
        SecuritySetupDTO securitySetupDTO = new SecuritySetupDTO();
        securitySetupDTO.setUsername("unknown");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        String result = userService.setSecurityQuestion(securitySetupDTO);
        assertEquals("User not found", result);
    }

    @Test
    void testUpdateSecurityQuestion_whenUserExists() {
        User user = new User();

        SecuritySetupDTO securitySetupDTO = new SecuritySetupDTO();
        securitySetupDTO.setSecurityQuestion("What is your favorite food?");
        securitySetupDTO.setSecurityAnswer("Pizza");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.updateSecurityQuestion(1L, securitySetupDTO);
        assertEquals("Security question updated successfully", result);
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteSecurityQuestion_whenUserExists() {
        User user = new User();
        user.setSecurityQuestion("Question");
        user.setSecurityAnswer("Answer");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.deleteSecurityQuestion(1L);
        assertEquals("Security question deleted successfully", result);
        assertNull(user.getSecurityQuestion());
        assertNull(user.getSecurityAnswer());
        verify(userRepository).save(user);
    }

    @Test
    void testUploadAvatar_whenFileIsValid() throws IOException {
        User user = new User();
        MockMultipartFile file = new MockMultipartFile(
                "avatar", "avatar.jpg", "image/jpeg", "dummy image content".getBytes());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String avatarUrl = userService.uploadAvatar(1L, file);
        assertTrue(avatarUrl.startsWith("/avatars/"));
        assertNotNull(user.getAvatar());
        verify(userRepository).save(user);
    }

    @Test
    void testUploadAvatar_whenFileTypeIsInvalid() {
        User user = new User();
        MockMultipartFile file = new MockMultipartFile(
                "avatar", "avatar.txt", "text/plain", "dummy content".getBytes());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.uploadAvatar(1L, file));
        assertEquals("Only JPEG, JPG, and PNG image types are allowed.", exception.getMessage());
    }

    @Test
    void testDeleteAvatar_whenUserExists() {
        User user = new User();
        user.setAvatar("/avatars/avatar.jpg");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String result = userService.deleteAvatar(1L);
        assertEquals("Avatar deleted successfully", result);
        assertNull(user.getAvatar());
        verify(userRepository).save(user);
    }

    @Test
    void testGetAvatar_whenAvatarExists() {
        User user = new User();
        user.setAvatar("/avatars/avatar.jpg");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        String avatarUrl = userService.getAvatar(1L);
        assertEquals("/avatars/avatar.jpg", avatarUrl);
    }

    @Test
    void testGetAvatar_whenAvatarDoesNotExist() {
        User user = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getAvatar(1L));
        assertEquals("Avatar not found", exception.getMessage());
    }

    @Test
    void testGetUserByUsername_whenUserExists() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testGetUserByUsername_whenUserDoesNotExist() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserByUsername("unknown");
        assertFalse(foundUser.isPresent());
    }
}