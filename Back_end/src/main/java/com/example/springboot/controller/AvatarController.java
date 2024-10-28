package com.example.springboot.controller;

import com.example.springboot.model.Avatar;
import com.example.springboot.repository.AvatarRepository;
import com.example.springboot.service.BaiduAvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;


@RestController
@RequestMapping("/user/avatar")
public class AvatarController {

    @Autowired
    private BaiduAvatarService baiduAvatarService;

    @Autowired
    private AvatarRepository avatarRepository;

    @PostMapping("/generate")
    public ResponseEntity<String> generateTemporaryAvatar(@RequestParam String keyword) {
        try {

            String imageUrl = baiduAvatarService.generateTemporaryAvatar(keyword);
            return ResponseEntity.ok( imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error generating avatar: " + e.getMessage());
        }
    }


    @PostMapping("/save")
    public ResponseEntity<String> saveAvatar(@RequestParam Long userId, @RequestParam String imageUrl, @RequestParam String keyword) {

        String savedUrl = baiduAvatarService.saveAvatar(userId, imageUrl, keyword);
        return ResponseEntity.ok("Avatar saved successfully.");
    }

    @GetMapping("/view/{userId}")
    public ResponseEntity<?> viewAvatar(@PathVariable Long userId) {
        Optional<Avatar> avatar = baiduAvatarService.getAvatarByUserId(userId);
        if (avatar.isPresent()) {
            return ResponseEntity.ok(avatar.get());
        } else {
            return ResponseEntity.status(404).body("Avatar not found for userId: " + userId);
        }
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long userId) {
        boolean isDeleted = baiduAvatarService.deleteAvatarByUserId(userId);
        if (isDeleted) {
            return ResponseEntity.ok("Avatar deleted successfully for userId: " + userId);
        } else {
            return ResponseEntity.status(404).body("Avatar not found for userId: " + userId);
        }
    }
}
