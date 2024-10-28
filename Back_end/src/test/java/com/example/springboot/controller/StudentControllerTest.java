package com.example.springboot.controller;

import com.example.springboot.model.Student;
import com.example.springboot.model.User;
import com.example.springboot.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setStudentId(1L);

        when(studentService.createStudent("testUser", "testPassword")).thenReturn(student);

        mockMvc.perform(post("/students/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\", \"password\":\"testPassword\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Student created successfully"))
                .andExpect(jsonPath("$.student_id").value("1"));

        verify(studentService, times(1)).createStudent("testUser", "testPassword");
    }

    @Test
    void testGetStudentById_Found() throws Exception {
        Long studentId = 1L;
        Student student = new Student();
        student.setStudentId(studentId);
        student.setFacility("Engineering");
        student.setMajor("Computer Science");

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        student.setUser(user);

        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.student_id").value(studentId))
                .andExpect(jsonPath("$.facility").value("Engineering"))
                .andExpect(jsonPath("$.major").value("Computer Science"))
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        Long studentId = 1L;

        when(studentService.getStudentById(studentId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/students/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Student not found"));

        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void testUpdateStudent() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(put("/students/update/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\", \"facility\":\"Business\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student updated successfully"));


        verify(studentService, times(1)).updateStudent(
                eq(studentId),
                eq("updatedUser"),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                nullable(String.class),
                eq("Business"),
                nullable(String.class)
        );
    }

    @Test
    void testDeleteStudent() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(delete("/students/delete/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Student deleted successfully"));

        verify(studentService, times(1)).deleteStudent(studentId);
    }

    @Test
    void testGetAllStudents() throws Exception {
        Student student1 = new Student();
        student1.setStudentId(1L);
        student1.setFacility("Engineering");
        student1.setMajor("Computer Science");

        Student student2 = new Student();
        student2.setStudentId(2L);
        student2.setFacility("Business");
        student2.setMajor("Marketing");

        List<Student> students = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].student_id").value(1))
                .andExpect(jsonPath("$[0].facility").value("Engineering"))
                .andExpect(jsonPath("$[0].major").value("Computer Science"))
                .andExpect(jsonPath("$[1].student_id").value(2))
                .andExpect(jsonPath("$[1].facility").value("Business"))
                .andExpect(jsonPath("$[1].major").value("Marketing"));

        verify(studentService, times(1)).getAllStudents();
    }
}