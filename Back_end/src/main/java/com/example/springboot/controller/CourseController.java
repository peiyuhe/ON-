package com.example.springboot.controller;

import com.example.springboot.model.Course;
import com.example.springboot.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok("Course created successfully");
    }

    @PutMapping("/update/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Long courseId, @RequestBody Course courseDetails) {
        courseService.updateCourse(courseId, courseDetails);
        return ResponseEntity.ok("Course updated successfully");
    }

    @DeleteMapping("/delete/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.ok("Course deleted successfully");
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable Long courseId) {
        Course course = courseService.getCourse(courseId);
        return ResponseEntity.ok(course);
    }
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Course>> getCoursesByTeacherId(@PathVariable Long teacherId) {
        List<Course> courses = courseService.getCoursesByTeacherId(teacherId);
        return ResponseEntity.ok(courses);
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
}