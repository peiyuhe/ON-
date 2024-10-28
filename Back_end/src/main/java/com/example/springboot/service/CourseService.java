package com.example.springboot.service;

import com.example.springboot.model.Course;
import com.example.springboot.model.Teacher;
import com.example.springboot.repository.CourseRepository;
import com.example.springboot.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow();
    }

    @Transactional
    public Course updateCourse(Long courseId, Course courseDetails) {
        Course course = courseRepository.findById(courseId).orElseThrow();

        course.setCourseName(courseDetails.getCourseName());
        course.setCourseDescription(courseDetails.getCourseDescription());

        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        courseRepository.delete(course);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getCoursesByTeacherId(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherId));
        return courseRepository.findByTeacher(teacher);
    }
}