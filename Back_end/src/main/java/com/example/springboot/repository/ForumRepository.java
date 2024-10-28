package com.example.springboot.repository;

import com.example.springboot.model.Forum;
import com.example.springboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {
    List<Forum> findByStudent(Student student);
    List<Forum> findByCourse_CourseId(Long courseId);

}