package com.example.springboot.repository;

import com.example.springboot.model.Student;
import com.example.springboot.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByUser(com.example.springboot.model.User user);
}
