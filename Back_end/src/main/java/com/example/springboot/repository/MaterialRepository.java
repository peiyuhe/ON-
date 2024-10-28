package com.example.springboot.repository;

import com.example.springboot.model.Course;
import com.example.springboot.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByCourse(Course course);
}
