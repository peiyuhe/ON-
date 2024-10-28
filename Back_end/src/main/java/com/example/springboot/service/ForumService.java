package com.example.springboot.service;

import com.example.springboot.dto.PostQuestionDTO;
import com.example.springboot.model.Course;
import com.example.springboot.model.Forum;
import com.example.springboot.model.Student;
import com.example.springboot.repository.CourseRepository;
import com.example.springboot.repository.ForumRepository;
import com.example.springboot.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ForumService {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private QianfanService qianfanService;

    @Transactional
    public Forum createForum(Forum forum) {
        return forumRepository.save(forum);
    }

    @Transactional
    public Forum getForum(Long forumId) {
        return forumRepository.findById(forumId).orElseThrow();
    }

    @Transactional
    public Forum updateForum(Long forumId, Forum forumDetails) {
        Forum forum = forumRepository.findById(forumId).orElseThrow();

        forum.setQuestion(forumDetails.getQuestion());
        forum.setAiAnswer(forumDetails.getAiAnswer());
        forum.setTeacherAnswer(forumDetails.getTeacherAnswer());
        forum.setPostedAt(forumDetails.getPostedAt());
        forum.setAnsweredAt(forumDetails.getAnsweredAt());

        return forumRepository.save(forum);
    }

    @Transactional
    public void deleteForum(Long forumId) {
        Forum forum = forumRepository.findById(forumId).orElseThrow();
        forumRepository.delete(forum);
    }

    @Transactional
    public List<Forum> getForumsByStudent(Long studentId) {
        return forumRepository.findByStudent(new Student(studentId));
    }


    public Forum postQuestion(PostQuestionDTO request) throws IOException {
        Optional<Course> courseOpt = courseRepository.findById(request.getCourseId());
        Optional<Student> studentOpt = studentRepository.findById(request.getStudentId());

        if (!courseOpt.isPresent()) {
            throw new IllegalArgumentException("课程不存在，ID: " + request.getCourseId());
        }
        if (!studentOpt.isPresent()) {
            throw new IllegalArgumentException("学生不存在，ID: " + request.getStudentId());
        }

        Forum forum = new Forum();
        forum.setCourse(courseOpt.get());
        forum.setStudent(studentOpt.get());
        forum.setQuestion(request.getQuestion());
        forum.setPostedAt(LocalDateTime.now());

        String aiAnswer = qianfanService.getAIAnswer(request.getQuestion());
        forum.setAiAnswer(aiAnswer);
        forum.setAnsweredAt(LocalDateTime.now());

        forumRepository.save(forum);

        return forum;
    }
    public List<Forum> getAllForums() {
        return forumRepository.findAll();
    }
}