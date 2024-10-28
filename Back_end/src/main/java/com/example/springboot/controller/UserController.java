package com.example.springboot.controller;
import com.example.springboot.model.User;
import com.example.springboot.dto.SecuritySetupDTO;
import com.example.springboot.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check-security/{userId}")
    public ResponseEntity<?> checkSecurityQuestion(@PathVariable Long userId) {
        boolean isSet = userService.hasSetSecurityQuestion(userId);

        if (isSet) {
            return ResponseEntity.ok("Security question is already set.");
        } else {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("Please set your security question.");
        }
    }


    @PostMapping("/set-security-question")
    public ResponseEntity<String> setSecurityQuestion(@RequestBody SecuritySetupDTO securitySetupDTO) {
        String response = userService.setSecurityQuestion(securitySetupDTO);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update-security-question/{userId}")
    public ResponseEntity<String> updateSecurityQuestion(@PathVariable Long userId, @RequestBody SecuritySetupDTO securitySetupDTO) {
        try {
            String response = userService.updateSecurityQuestion(userId, securitySetupDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating security question: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-security-question/{userId}")
    public ResponseEntity<String> deleteSecurityQuestion(@PathVariable Long userId) {
        try {
            String response = userService.deleteSecurityQuestion(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting security question: " + e.getMessage());
        }
    }


    @PostMapping("/upload-avatar/{userId}")
    public ResponseEntity<String> uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            // 调用 UserService 中的 uploadAvatar 方法
            String avatarUrl = userService.uploadAvatar(userId, file);

            return ResponseEntity.ok("Avatar uploaded successfully: " + avatarUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file type: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading avatar: " + e.getMessage());
        }
    }

    @GetMapping("/download-avatar/{userId}")
    public ResponseEntity<Resource> downloadAvatar(@PathVariable Long userId, @RequestParam("filePath") String filePath) {
        try {
            Resource resource = userService.downloadAvatar(userId, filePath);
            String contentType = Files.probeContentType(resource.getFile().toPath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PutMapping("/update-avatar/{userId}")
    public ResponseEntity<String> updateAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            String avatarUrl = userService.uploadAvatar(userId, file);
            return ResponseEntity.ok("Avatar updated successfully: " + avatarUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating avatar: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete-avatar/{userId}")
    public ResponseEntity<String> deleteAvatar(@PathVariable Long userId) {
        try {
            String response = userService.deleteAvatar(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting avatar: " + e.getMessage());
        }
    }

    @GetMapping("/get-avatar/{userId}")
    public ResponseEntity<String> getAvatar(@PathVariable Long userId) {
        try {
            String avatarUrl = userService.getAvatar(userId);
            return ResponseEntity.ok(avatarUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving avatar: " + e.getMessage());
        }
    }
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            Optional<User> user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        }
    }
}