package com.example.springboot.service;

import com.example.springboot.dto.PostQuestionDTO;
import com.example.springboot.model.Course;
import com.example.springboot.model.Forum;
import com.example.springboot.model.Student;
import com.example.springboot.repository.CourseRepository;
import com.example.springboot.repository.ForumRepository;
import com.example.springboot.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForumServiceTest {

    @Mock
    private ForumRepository forumRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private QianfanService qianfanService;

    @InjectMocks
    private ForumService forumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateForum() {
        Forum forum = new Forum();
        forum.setQuestion("What is AI?");

        when(forumRepository.save(forum)).thenReturn(forum);

        Forum createdForum = forumService.createForum(forum);

        assertNotNull(createdForum);
        assertEquals("What is AI?", createdForum.getQuestion());
        verify(forumRepository, times(1)).save(forum);
    }

    @Test
    void testGetForum() {
        Forum forum = new Forum();
        forum.setForumId(1L);
        forum.setQuestion("What is AI?");

        when(forumRepository.findById(1L)).thenReturn(Optional.of(forum));

        Forum foundForum = forumService.getForum(1L);

        assertNotNull(foundForum);
        assertEquals(1L, foundForum.getForumId());
        assertEquals("What is AI?", foundForum.getQuestion());
        verify(forumRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateForum() {
        Forum existingForum = new Forum();
        existingForum.setForumId(1L);
        existingForum.setQuestion("What is AI?");
        existingForum.setPostedAt(LocalDateTime.now());

        Forum updatedDetails = new Forum();
        updatedDetails.setQuestion("What is Machine Learning?");
        updatedDetails.setAiAnswer("AI Answer");
        updatedDetails.setTeacherAnswer("Teacher Answer");

        when(forumRepository.findById(1L)).thenReturn(Optional.of(existingForum));
        when(forumRepository.save(existingForum)).thenReturn(existingForum);

        Forum updatedForum = forumService.updateForum(1L, updatedDetails);

        assertNotNull(updatedForum);
        assertEquals("What is Machine Learning?", updatedForum.getQuestion());
        assertEquals("AI Answer", updatedForum.getAiAnswer());
        assertEquals("Teacher Answer", updatedForum.getTeacherAnswer());
        verify(forumRepository, times(1)).save(existingForum);
    }

    @Test
    void testDeleteForum() {
        Forum forum = new Forum();
        forum.setForumId(1L);

        when(forumRepository.findById(1L)).thenReturn(Optional.of(forum));

        forumService.deleteForum(1L);

        verify(forumRepository, times(1)).delete(forum);
    }

    @Test
    void testGetForumsByStudent() {
        Student student = new Student();
        student.setStudentId(1L);

        Forum forum1 = new Forum();
        forum1.setForumId(1L);
        forum1.setStudent(student);

        Forum forum2 = new Forum();
        forum2.setForumId(2L);
        forum2.setStudent(student);

        when(forumRepository.findByStudent(student)).thenReturn(Arrays.asList(forum1, forum2));

        List<Forum> forums = forumService.getForumsByStudent(1L);

        assertEquals(2, forums.size());
        verify(forumRepository, times(1)).findByStudent(student);
    }

    @Test
    void testPostQuestion() throws IOException {
        PostQuestionDTO request = new PostQuestionDTO();
        request.setCourseId(1L);
        request.setStudentId(1L);
        request.setQuestion("What is AI?");

        Course course = new Course();
        course.setCourseId(1L);

        Student student = new Student();
        student.setStudentId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(qianfanService.getAIAnswer("What is AI?")).thenReturn("AI-generated answer");

        Forum forum = forumService.postQuestion(request);

        assertNotNull(forum);
        assertEquals("What is AI?", forum.getQuestion());
        assertEquals("AI-generated answer", forum.getAiAnswer());
        verify(forumRepository, times(1)).save(any(Forum.class));
    }

    @Test
    void testPostQuestion_CourseNotFound() {
        PostQuestionDTO request = new PostQuestionDTO();
        request.setCourseId(1L);
        request.setStudentId(1L);
        request.setQuestion("What is AI?");

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> forumService.postQuestion(request));
        assertEquals("课程不存在，ID: 1", exception.getMessage());
    }

    @Test
    void testPostQuestion_StudentNotFound() {
        PostQuestionDTO request = new PostQuestionDTO();
        request.setCourseId(1L);
        request.setStudentId(1L);
        request.setQuestion("What is AI?");

        Course course = new Course();
        course.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> forumService.postQuestion(request));
        assertEquals("学生不存在，ID: 1", exception.getMessage());
    }
}