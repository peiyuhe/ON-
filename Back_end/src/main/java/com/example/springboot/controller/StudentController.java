package com.example.springboot.controller;

import com.example.springboot.model.Student;
import com.example.springboot.model.Submission;
import com.example.springboot.model.User;
import com.example.springboot.service.StudentService;
import com.example.springboot.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private SubmissionService submissionService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudent(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Student student = studentService.createStudent(username, password);
        return ResponseEntity.status(201).body(Map.of(
                "message", "Student created successfully",
                "student_id", student.getStudentId().toString()
        ));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentById(@PathVariable Long studentId) {
        Optional<Student> studentOptional = studentService.getStudentById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            User user = student.getUser();


            Map<String, Object> studentData = new HashMap<>();
            studentData.put("user_id", student.getStudentId());
            studentData.put("student_id", student.getStudentId());
            studentData.put("facility", student.getFacility());
            studentData.put("major", student.getMajor());


            studentData.put("username", user != null ? user.getUsername() : null);
            studentData.put("email", user != null ? user.getEmail() : null);
            studentData.put("phone", user != null ? user.getPhone() : null);
            studentData.put("avatar", user != null ? user.getAvatar() : null);
            studentData.put("birthDay", user != null ? (user.getBirthDay() != null ? user.getBirthDay().toString() : null) : null);
            studentData.put("gender", user != null ? (user.getGender() != null ? user.getGender().name() : null) : null);
            studentData.put("role", user != null ? (user.getRole() != null ? user.getRole().name() : null) : null);
            studentData.put("securityQuestion", user!=null?user.getSecurityQuestion():null);
            studentData.put("securityAnswer", user!=null?user.getSecurityAnswer():null);


            return ResponseEntity.ok(studentData);
        } else {

            return ResponseEntity.status(404).body(Map.of(
                    "error", "Student not found"
            ));
        }
    }
    @PutMapping("/update/{studentId}")
    public ResponseEntity<?> updateStudent(@PathVariable Long studentId, @RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String phone = request.get("phone");
        String avatar = request.get("avatar");
        String birthDayStr = request.get("birthDay");
        String genderStr = request.get("gender");
        String facility = request.get("facility");
        String major = request.get("major");

        try {
            studentService.updateStudent(studentId, username, password, email, phone, avatar, birthDayStr, genderStr, facility, major);
            return ResponseEntity.ok(Map.of(
                    "message", "Student updated successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long studentId) {
        try {
            studentService.deleteStudent(studentId);
            return ResponseEntity.ok(Map.of(
                    "message", "Student deleted successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllStudents() {
        List<Student> students = studentService.getAllStudents();

        List<Map<String, Object>> studentDataList = new ArrayList<>();

        for (Student student : students) {
            User user = student.getUser();

            Map<String, Object> studentData = new HashMap<>();
            studentData.put("student_id", student.getStudentId());
            studentData.put("facility", student.getFacility());
            studentData.put("major", student.getMajor());

            studentData.put("username", user != null ? user.getUsername() : null);
            studentData.put("email", user != null ? user.getEmail() : null);
            studentData.put("phone", user != null ? user.getPhone() : null);
            studentData.put("avatar", user != null ? user.getAvatar() : null);
            studentData.put("birthDay", user != null ? (user.getBirthDay() != null ? user.getBirthDay().toString() : null) : null);
            studentData.put("gender", user != null ? (user.getGender() != null ? user.getGender().name() : null) : null);
            studentData.put("role", user != null ? (user.getRole() != null ? user.getRole().name() : null) : null);

            studentDataList.add(studentData);
        }

        return ResponseEntity.ok(studentDataList);
    }
}