package com.example.springboot.service;

import com.example.springboot.model.Student;
import com.example.springboot.model.User;
import com.example.springboot.repository.StudentRepository;
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

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent() {
        String username = "student1";
        String password = "password";
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encodedPassword);
        user.setRole(User.Role.STUDENT);

        Student student = new Student();
        student.setUser(user);

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent(username, password);

        assertNotNull(createdStudent);
        assertEquals(username, createdStudent.getUser().getUsername());
        assertEquals(encodedPassword, createdStudent.getUser().getPasswordHash());
        assertEquals(User.Role.STUDENT, createdStudent.getUser().getRole());
    }

    @Test
    void testGetStudentById_whenStudentExists() {
        Student student = new Student();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> foundStudent = studentService.getStudentById(1L);

        assertTrue(foundStudent.isPresent());
        assertEquals(student, foundStudent.get());
    }

    @Test
    void testGetStudentById_whenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Student> foundStudent = studentService.getStudentById(1L);

        assertFalse(foundStudent.isPresent());
    }

    @Test
    void testUpdateStudent_whenStudentExists() {
        Student student = new Student();
        User user = new User();
        student.setUser(user);

        String newUsername = "newUsername";
        String newPassword = "newPassword";
        String encodedPassword = "encodedPassword";
        String email = "newEmail@example.com";
        String phone = "1234567890";
        String avatar = "/avatars/newAvatar.png";
        String birthDayStr = "2000-01-01T00:00:00";
        String genderStr = "MALE";
        String facility = "New Facility";
        String major = "New Major";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.updateStudent(1L, newUsername, newPassword, email, phone, avatar, birthDayStr, genderStr, facility, major);

        assertEquals(newUsername, student.getUser().getUsername());
        assertEquals(encodedPassword, student.getUser().getPasswordHash());
        assertEquals(email, student.getUser().getEmail());
        assertEquals(phone, student.getUser().getPhone());
        assertEquals(avatar, student.getUser().getAvatar());
        assertEquals(LocalDateTime.parse(birthDayStr), student.getUser().getBirthDay());
        assertEquals(User.Gender.MALE, student.getUser().getGender());
        assertEquals(facility, student.getFacility());
        assertEquals(major, student.getMajor());

        verify(userRepository).save(user);
        verify(studentRepository).save(student);
    }

    @Test
    void testUpdateStudent_whenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                studentService.updateStudent(1L, "username", "password", null, null, null, null, null, null, null));

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void testDeleteStudent_whenStudentExists() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        studentService.deleteStudent(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void testDeleteStudent_whenStudentDoesNotExist() {
        when(studentRepository.existsById(1L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.deleteStudent(1L));

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        Student student2 = new Student();

        when(studentRepository.findAll()).thenReturn(List.of(student1, student2));

        List<Student> students = studentService.getAllStudents();

        assertEquals(2, students.size());
        assertEquals(student1, students.get(0));
        assertEquals(student2, students.get(1));
    }

    @Test
    void testGetStudent_whenStudentExists() {
        Student student = new Student();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student foundStudent = studentService.getStudent(1L);

        assertNotNull(foundStudent);
        assertEquals(student, foundStudent);
    }

    @Test
    void testGetStudent_whenStudentDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> studentService.getStudent(1L));

        assertEquals("Student not found with id: 1", exception.getMessage());
    }
}