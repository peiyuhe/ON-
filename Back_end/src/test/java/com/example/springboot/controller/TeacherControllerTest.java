package com.example.springboot.controller;

import com.example.springboot.model.Teacher;
import com.example.springboot.model.User;
import com.example.springboot.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TeacherController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTeacher() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "teacher1");
        request.put("password", "password");

        Teacher teacher = new Teacher();
        teacher.setTeacherId(1L);

        when(teacherService.createTeacher("teacher1", "password")).thenReturn(teacher);

        mockMvc.perform(post("/teachers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("teacher created successfully"))
                .andExpect(jsonPath("$.teacher_id").value("1"));
    }

    @Test
    public void testGetTeacherById_Success() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1L);
        teacher.setFacility("Math Department");

        User user = new User();
        user.setUsername("teacher1");
        user.setEmail("teacher1@example.com");
        teacher.setUser(user);

        when(teacherService.getTeacherById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.facility").value("Math Department"))
                .andExpect(jsonPath("$.username").value("teacher1"))
                .andExpect(jsonPath("$.email").value("teacher1@example.com"));
    }

    @Test
    public void testGetTeacherById_NotFound() throws Exception {
        when(teacherService.getTeacherById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Teacher not found"));
    }

    @Test
    public void testUpdateTeacher_Success() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "updatedTeacher");
        request.put("password", "newPassword");

        Mockito.doNothing().when(teacherService).updateTeacher(
                any(Long.class), any(String.class), any(String.class), any(String.class), any(String.class),
                any(String.class), any(String.class), any(String.class), any(String.class));

        mockMvc.perform(put("/teachers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Teacher updated successfully"));
    }

    @Test
    public void testUpdateTeacher_NotFound() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "updatedTeacher");
        request.put("password", "newPassword");

        // 模拟 teacherService.updateTeacher 方法抛出异常
        Mockito.doThrow(new RuntimeException("Teacher not found"))
                .when(teacherService).updateTeacher(
                        Mockito.eq(1L),
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString()
                );

        mockMvc.perform(put("/teachers/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Teacher not found"));
    }

    @Test
    public void testDeleteTeacher_Success() throws Exception {
        Mockito.doNothing().when(teacherService).deleteTeacher(1L);

        mockMvc.perform(delete("/teachers/delete/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("teacher deleted successfully"));
    }

    @Test
    public void testDeleteTeacher_NotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Teacher not found")).when(teacherService).deleteTeacher(1L);

        mockMvc.perform(delete("/teachers/delete/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Teacher not found"));
    }

    @Test
    public void testGetAllTeachers() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setTeacherId(1L);
        teacher1.setFacility("Math Department");

        User user1 = new User();
        user1.setUsername("teacher1");
        teacher1.setUser(user1);

        Teacher teacher2 = new Teacher();
        teacher2.setTeacherId(2L);
        teacher2.setFacility("Science Department");

        User user2 = new User();
        user2.setUsername("teacher2");
        teacher2.setUser(user2);

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/teachers/get/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacher_id").value(1))
                .andExpect(jsonPath("$[0].facility").value("Math Department"))
                .andExpect(jsonPath("$[0].username").value("teacher1"))
                .andExpect(jsonPath("$[1].teacher_id").value(2))
                .andExpect(jsonPath("$[1].facility").value("Science Department"))
                .andExpect(jsonPath("$[1].username").value("teacher2"));
    }
}