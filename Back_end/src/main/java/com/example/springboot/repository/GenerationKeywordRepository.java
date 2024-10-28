package com.example.springboot.repository;

import com.example.springboot.model.GenerationKeyword;
import com.example.springboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenerationKeywordRepository extends JpaRepository<GenerationKeyword, Long> {
    Optional<GenerationKeyword> findByStudent(Student student);

    void deleteByStudent(Student student);
}
