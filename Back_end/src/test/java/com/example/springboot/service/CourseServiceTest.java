package com.example.springboot.service;

import com.example.springboot.model.Course;
import com.example.springboot.model.Teacher;
import com.example.springboot.repository.CourseRepository;
import com.example.springboot.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCourse() {
        Course course = new Course();
        course.setCourseName("Math");
        course.setCourseDescription("Math Course");

        when(courseRepository.save(course)).thenReturn(course);

        Course createdCourse = courseService.createCourse(course);

        assertNotNull(createdCourse);
        assertEquals("Math", createdCourse.getCourseName());
        assertEquals("Math Course", createdCourse.getCourseDescription());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testGetCourse_whenCourseExists() {
        Course course = new Course();
        course.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course foundCourse = courseService.getCourse(1L);

        assertNotNull(foundCourse);
        assertEquals(1L, foundCourse.getCourseId());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourse_whenCourseDoesNotExist() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.getCourse(1L));
    }

    @Test
    void testUpdateCourse_whenCourseExists() {
        Course existingCourse = new Course();
        existingCourse.setCourseId(1L);
        existingCourse.setCourseName("Math");

        Course courseDetails = new Course();
        courseDetails.setCourseName("Physics");
        courseDetails.setCourseDescription("Physics Course");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);

        Course updatedCourse = courseService.updateCourse(1L, courseDetails);

        assertNotNull(updatedCourse);
        assertEquals("Physics", updatedCourse.getCourseName());
        assertEquals("Physics Course", updatedCourse.getCourseDescription());
        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    void testUpdateCourse_whenCourseDoesNotExist() {
        Course courseDetails = new Course();
        courseDetails.setCourseName("Physics");
        courseDetails.setCourseDescription("Physics Course");

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.updateCourse(1L, courseDetails));
    }

    @Test
    void testDeleteCourse_whenCourseExists() {
        Course course = new Course();
        course.setCourseId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    void testDeleteCourse_whenCourseDoesNotExist() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.deleteCourse(1L));
    }

    @Test
    void testGetAllCourses() {
        Course course1 = new Course();
        Course course2 = new Course();

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        List<Course> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testGetCoursesByTeacherId_whenTeacherExists() {
        Teacher teacher = new Teacher();
        teacher.setTeacherId(1L);

        Course course1 = new Course();
        course1.setTeacher(teacher);
        Course course2 = new Course();
        course2.setTeacher(teacher);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseRepository.findByTeacher(teacher)).thenReturn(List.of(course1, course2));

        List<Course> courses = courseService.getCoursesByTeacherId(1L);

        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findByTeacher(teacher);
    }

    @Test
    void testGetCoursesByTeacherId_whenTeacherDoesNotExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                courseService.getCoursesByTeacherId(1L));

        assertEquals("Teacher not found with ID: 1", exception.getMessage());
    }
}