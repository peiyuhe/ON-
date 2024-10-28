package com.example.springboot.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "avatar")
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long avatarId;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "avatar_keyword", nullable = false)
    private String avatarKeyword;

    @Column(name = "avatar_data", nullable = false)
    private String avatarData;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
    }
}
