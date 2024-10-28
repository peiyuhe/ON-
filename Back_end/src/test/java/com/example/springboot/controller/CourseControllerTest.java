package com.example.springboot.controller;

import com.example.springboot.model.Course;
import com.example.springboot.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse() throws Exception {
        Course course = new Course();
        course.setCourseName("Test Course");

        when(courseService.createCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/courses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseName\":\"Test Course\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Course created successfully"));

        verify(courseService, times(1)).createCourse(any(Course.class));
    }

    @Test
    void testUpdateCourse() throws Exception {
        Long courseId = 1L;
        Course courseDetails = new Course();
        courseDetails.setCourseName("Updated Course");

        mockMvc.perform(put("/courses/update/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"courseName\":\"Updated Course\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Course updated successfully"));

        verify(courseService, times(1)).updateCourse(eq(courseId), any(Course.class));
    }

    @Test
    void testDeleteCourse() throws Exception {
        Long courseId = 1L;

        mockMvc.perform(delete("/courses/delete/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Course deleted successfully"));

        verify(courseService, times(1)).deleteCourse(courseId);
    }

    @Test
    void testGetCourse() throws Exception {
        Long courseId = 1L;
        Course course = new Course();
        course.setCourseId(courseId);
        course.setCourseName("Test Course");

        when(courseService.getCourse(courseId)).thenReturn(course);

        mockMvc.perform(get("/courses/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.courseName").value("Test Course"));

        verify(courseService, times(1)).getCourse(courseId);
    }

    @Test
    void testGetCoursesByTeacherId() throws Exception {
        Long teacherId = 1L;
        Course course1 = new Course();
        course1.setCourseName("Course 1");
        Course course2 = new Course();
        course2.setCourseName("Course 2");

        List<Course> courses = Arrays.asList(course1, course2);
        when(courseService.getCoursesByTeacherId(teacherId)).thenReturn(courses);

        mockMvc.perform(get("/courses/teacher/{teacherId}", teacherId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Course 1"))
                .andExpect(jsonPath("$[1].courseName").value("Course 2"));

        verify(courseService, times(1)).getCoursesByTeacherId(teacherId);
    }

    @Test
    void testGetAllCourses() throws Exception {
        Course course1 = new Course();
        course1.setCourseName("Course 1");
        Course course2 = new Course();
        course2.setCourseName("Course 2");

        List<Course> courses = Arrays.asList(course1, course2);
        when(courseService.getAllCourses()).thenReturn(courses);

        mockMvc.perform(get("/courses/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Course 1"))
                .andExpect(jsonPath("$[1].courseName").value("Course 2"));

        verify(courseService, times(1)).getAllCourses();
    }
}