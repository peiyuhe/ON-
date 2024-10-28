package com.example.springboot.service;

import com.example.springboot.model.Student;
import com.example.springboot.model.User;
import com.example.springboot.repository.StudentRepository;
import com.example.springboot.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student createStudent(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(User.Role.STUDENT);
        Student student = new Student();
        student.setUser(user);

        return studentRepository.save(student);
    }


    public Optional<Student> getStudentById(Long studentId) {
        return studentRepository.findById(studentId);
    }


    public void updateStudent(Long studentId, String username, String password, String email, String phone, String avatar, String birthDayStr, String genderStr, String facility, String major) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            User user = student.getUser();


            if (username != null) {
                user.setUsername(username);
            }
            if (password != null) {
                user.setPasswordHash(passwordEncoder.encode(password));
            }
            if (email != null) {
                user.setEmail(email);
            }
            if (phone != null) {
                user.setPhone(phone);
            }
            if (avatar != null) {
                user.setAvatar(avatar);
            }
            if (birthDayStr != null) {
                try {
                    LocalDate birthDay = LocalDate.parse(birthDayStr);
                    user.setBirthDay(LocalDate.from(birthDay));
                } catch (DateTimeParseException e) {
                    throw new RuntimeException("Invalid birthDay format");
                }
            }
            if (genderStr != null) {
                try {
                    User.Gender gender = User.Gender.valueOf(genderStr.toUpperCase());
                    user.setGender(gender);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid gender value");
                }
            }


            if (facility != null) {
                student.setFacility(facility);
            }
            if (major != null) {
                student.setMajor(major);
            }


            userRepository.save(user);
            studentRepository.save(student);
        } else {
            throw new RuntimeException("Student not found");
        }
    }
    public void deleteStudent(Long studentId) {
        if (studentRepository.existsById(studentId)) {
            studentRepository.deleteById(studentId);
        } else {
            throw new RuntimeException("Student not found");
        }
    }
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    public Student getStudent(Long studentId) {
        // 使用 studentId 查找对应的 Student 实体
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
    }
}
