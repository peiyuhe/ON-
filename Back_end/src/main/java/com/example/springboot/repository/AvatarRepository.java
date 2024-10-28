package com.example.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springboot.model.Avatar;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Avatar findByUserId(Long userId);
}
