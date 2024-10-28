package com.example.springboot.repository;

import com.example.springboot.model.Student;
import com.example.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUser(com.example.springboot.model.User user);
}