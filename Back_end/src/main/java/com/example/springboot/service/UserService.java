package com.example.springboot.service;

import com.example.springboot.dto.SecuritySetupDTO;
import com.example.springboot.model.User;
import com.example.springboot.repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final String AVATAR_DIRECTORY = Paths.get("src", "main", "resources", "static", "avatar").toString();


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean hasSetSecurityQuestion(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getSecurityQuestion() != null && user.getSecurityAnswer() != null;
    }

    public String setSecurityQuestion(SecuritySetupDTO securitySetupDTO) {
        Optional<User> userOpt = userRepository.findByUsername(securitySetupDTO.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setSecurityQuestion(securitySetupDTO.getSecurityQuestion());
            user.setSecurityAnswer(securitySetupDTO.getSecurityAnswer());
            userRepository.save(user);
            return "Security question set successfully";
        } else {
            return "User not found";
        }
    }

    public String updateSecurityQuestion(Long userId, SecuritySetupDTO securitySetupDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(securitySetupDTO.getSecurityQuestion() + "");
        user.setSecurityQuestion(securitySetupDTO.getSecurityQuestion());
        user.setSecurityAnswer(securitySetupDTO.getSecurityAnswer());
        userRepository.save(user);

        return "Security question updated successfully";
    }
    public String deleteSecurityQuestion(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setSecurityQuestion(null);
        user.setSecurityAnswer(null);
        userRepository.save(user);

        return "Security question deleted successfully";
    }

    public Resource downloadAvatar(Long userId, String filePath) throws IOException {
        Path avatarPath = Paths.get(AVATAR_DIRECTORY, "usr" + userId, filePath).normalize();
        Resource resource = new UrlResource(avatarPath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("文件未找到: " + avatarPath.toString());
        }

        return resource;
    }

    public String uploadAvatar(Long userId, MultipartFile file) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg"))) {
            throw new IllegalArgumentException("Only JPEG, JPG, and PNG image types are allowed.");
        }
        Path uploadDirPath = Paths.get( AVATAR_DIRECTORY, "usr" + userId.toString());
        System.out.println(uploadDirPath);
        Files.createDirectories(uploadDirPath );
        if (Files.exists(uploadDirPath)) {
            System.out.println("Directory successfully created at: " + uploadDirPath.toAbsolutePath());
        } else {
            System.err.println("Failed to create directory.");
        }
        String fileName = file.getOriginalFilename();
        Path filePath = uploadDirPath .resolve(fileName);
        Files.write(filePath, file.getBytes());

        String avatarUrl = "/avatar/usr" + userId + "/" + fileName;
        user.setAvatar(avatarUrl);
        userRepository.save(user);

        return avatarUrl;
    }

    public String deleteAvatar(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setAvatar(null);
        userRepository.save(user);

        return "Avatar deleted successfully";
    }
    public String getAvatar(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAvatar() == null) {
            throw new RuntimeException("Avatar not found");
        }

        return user.getAvatar();
    }
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
