package com.example.springboot.repository;

import com.example.springboot.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
