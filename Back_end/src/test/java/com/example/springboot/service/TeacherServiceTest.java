package com.example.springboot.service;

import com.example.springboot.model.Teacher;
import com.example.springboot.model.User;
import com.example.springboot.repository.TeacherRepository;
import com.example.springboot.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTeacher() {
        String username = "teacher1";
        String password = "password";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encodedPassword);
        user.setRole(User.Role.TEACHER);

        Teacher teacher = new Teacher();
        teacher.setUser(user);

        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        Teacher createdTeacher = teacherService.createTeacher(username, password);

        assertNotNull(createdTeacher);
        assertEquals(username, createdTeacher.getUser().getUsername());
        assertEquals(encodedPassword, createdTeacher.getUser().getPasswordHash());
        assertEquals(User.Role.TEACHER, createdTeacher.getUser().getRole());
    }

    @Test
    void testGetTeacherById_whenTeacherExists() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Optional<Teacher> foundTeacher = teacherService.getTeacherById(1L);

        assertTrue(foundTeacher.isPresent());
        assertEquals(teacher, foundTeacher.get());
    }

    @Test
    void testGetTeacherById_whenTeacherDoesNotExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Teacher> foundTeacher = teacherService.getTeacherById(1L);

        assertFalse(foundTeacher.isPresent());
    }

    @Test
    void testUpdateTeacher_whenTeacherExists() {
        Teacher teacher = new Teacher();
        User user = new User();
        teacher.setUser(user);

        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String encodedPassword = "encodedPassword";
        String email = "newEmail@example.com";
        String phone = "1234567890";
        String avatar = "/avatars/newAvatar.png";
        String birthDayStr = "2000-01-01T00:00:00";
        String genderStr = "MALE";
        String facility = "New Facility";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        teacherService.updateTeacher(1L, newUsername, newPassword, email, phone, avatar, birthDayStr, genderStr, facility);

        assertEquals(newUsername, teacher.getUser().getUsername());
        assertEquals(encodedPassword, teacher.getUser().getPasswordHash());
        assertEquals(email, teacher.getUser().getEmail());
        assertEquals(phone, teacher.getUser().getPhone());
        assertEquals(avatar, teacher.getUser().getAvatar());
        assertEquals(LocalDateTime.parse(birthDayStr), teacher.getUser().getBirthDay());
        assertEquals(User.Gender.MALE, teacher.getUser().getGender());
        assertEquals(facility, teacher.getFacility());

        verify(userRepository).save(user);
        verify(teacherRepository).save(teacher);
    }

    @Test
    void testUpdateTeacher_whenTeacherDoesNotExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                teacherService.updateTeacher(1L, "username", "password", null, null, null, null, null, null));

        assertEquals("Teacher not found", exception.getMessage());
    }

    @Test
    void testDeleteTeacher_whenTeacherExists() {
        when(teacherRepository.existsById(1L)).thenReturn(true);

        teacherService.deleteTeacher(1L);

        verify(teacherRepository).deleteById(1L);
    }

    @Test
    void testDeleteTeacher_whenTeacherDoesNotExist() {
        when(teacherRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> teacherService.deleteTeacher(1L));

        assertEquals("teacher not found", exception.getMessage());
    }

    @Test
    void testGetAllTeachers() {
        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();

        when(teacherRepository.findAll()).thenReturn(List.of(teacher1, teacher2));

        List<Teacher> teachers = teacherService.getAllTeachers();

        assertEquals(2, teachers.size());
        assertEquals(teacher1, teachers.get(0));
        assertEquals(teacher2, teachers.get(1));
    }
}