package com.example.springboot.controller;

import com.example.springboot.model.Teacher;
import com.example.springboot.model.User;
import com.example.springboot.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTeacher(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        Teacher teacher = teacherService.createTeacher(username, password);
        return ResponseEntity.status(201).body(Map.of(
                "message", "teacher created successfully",
                "teacher_id", teacher.getTeacherId().toString()
        ));
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<?> getTeacherById(@PathVariable Long teacherId) {
        Optional<Teacher> teacherOptional = teacherService.getTeacherById(teacherId);
        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            User user = teacher.getUser();


            Map<String, Object> teacherData = new HashMap<>();
            teacherData.put("user_id", user.getUser_Id());
            teacherData.put("teacher_id", teacher.getTeacherId());
            teacherData.put("facility", teacher.getFacility());


            teacherData.put("username", user != null ? user.getUsername() : null);
            teacherData.put("email", user != null ? user.getEmail() : null);
            teacherData.put("phone", user != null ? user.getPhone() : null);
            teacherData.put("avatar", user != null ? user.getAvatar() : null);
            teacherData.put("birthDay", user != null ? (user.getBirthDay() != null ? user.getBirthDay().toString() : null) : null);
            teacherData.put("gender", user != null ? (user.getGender() != null ? user.getGender().name() : null) : null);
            teacherData.put("role", user != null ? (user.getRole() != null ? user.getRole().name() : null) : null);
            teacherData.put("securityQuestion", user!=null?user.getSecurityQuestion():null);
            teacherData.put("securityAnswer", user!=null?user.getSecurityAnswer():null);


            return ResponseEntity.ok(teacherData);
        } else {

            return ResponseEntity.status(404).body(Map.of(
                    "error", "Teacher not found"
            ));
        }
    }

    @PutMapping("/update/{teacherId}")
    public ResponseEntity<?> updateTeacher(@PathVariable Long teacherId, @RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String phone = request.get("phone");
        String avatar = request.get("avatar");
        String birthDayStr = request.get("birthDay");
        String genderStr = request.get("gender");
        String facility = request.get("facility");

        try {
            teacherService.updateTeacher(teacherId, username, password, email, phone, avatar, birthDayStr, genderStr, facility);
            return ResponseEntity.ok(Map.of(
                    "message", "Teacher updated successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete/{teacherId}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Long teacherId) {
        try {
            teacherService.deleteTeacher(teacherId);
            return ResponseEntity.ok(Map.of(
                    "message", "teacher deleted successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();

        List<Map<String, Object>> teacherDataList = new ArrayList<>();

        for (Teacher teacher : teachers) {
            User user = teacher.getUser();

            Map<String, Object> teacherData = new HashMap<>();
            teacherData.put("teacher_id", teacher.getTeacherId());
            teacherData.put("facility", teacher.getFacility());

            teacherData.put("username", user != null ? user.getUsername() : null);
            teacherData.put("email", user != null ? user.getEmail() : null);
            teacherData.put("phone", user != null ? user.getPhone() : null);
            teacherData.put("avatar", user != null ? user.getAvatar() : null);
            teacherData.put("birthDay", user != null ? (user.getBirthDay() != null ? user.getBirthDay().toString() : null) : null);
            teacherData.put("gender", user != null ? (user.getGender() != null ? user.getGender().name() : null) : null);
            teacherData.put("role", user != null ? (user.getRole() != null ? user.getRole().name() : null) : null);

            teacherDataList.add(teacherData);
        }

        return ResponseEntity.ok(teacherDataList);
    }
}
