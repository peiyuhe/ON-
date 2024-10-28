package com.example.springboot.service;

import com.example.springboot.dto.LoginDTO;
import com.example.springboot.dto.PasswordResetDTO;
import com.example.springboot.dto.UserRegistrationDTO;
import com.example.springboot.model.Student;
import com.example.springboot.model.Teacher;
import com.example.springboot.model.User;
import com.example.springboot.repository.StudentRepository;
import com.example.springboot.repository.TeacherRepository;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, StudentRepository studentRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User registerUser(UserRegistrationDTO registrationDTO) {
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));
        User.Role role = User.Role.valueOf(registrationDTO.getRole().toUpperCase());
        user.setRole(role);

        User savedUser = userRepository.save(user);
        if (role == User.Role.STUDENT) {
            createStudentProfile(savedUser);
        } else if (role == User.Role.TEACHER) {
            createTeacherProfile(savedUser);
        }

        return savedUser;
    }

    private void createStudentProfile(User user) {
        Student student = new Student();
        student.setUser(user);
        student.setFacility("Default College");
        student.setMajor("Default Major");
        studentRepository.save(student);
    }

    private void createTeacherProfile(User user) {
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setFacility("Default College");
        teacherRepository.save(teacher);
    }

    public Optional<Map<String, Object>> loginWithRole(LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByUsername(loginDTO.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getRole().toString());

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("token", token);


                if (user.getRole() == User.Role.STUDENT) {
                    Student student = studentRepository.findByUser(user);
                    response.put("student", student);
                } else if (user.getRole() == User.Role.TEACHER) {
                    Teacher teacher = teacherRepository.findByUser(user);
                    response.put("teacher", teacher);
                }

                return Optional.of(response);
            }
        }
        return Optional.empty();
    }
    //PasswordReset

    public String resetPassword(PasswordResetDTO passwordResetDTO) {
        Optional<User> userOpt = userRepository.findByUsername(passwordResetDTO.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (user.getSecurityAnswer().equals(passwordResetDTO.getSecurityAnswer())) {
                if (passwordResetDTO.getNewPassword().equals(passwordResetDTO.getConfirmPassword())) {
                    user.setPasswordHash(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
                    userRepository.save(user);
                    return "Password reset successful";
                } else {
                    return "Passwords do not match";
                }
            } else {
                return "Incorrect security answer";
            }
        }

        return "User not found";
    }
}


