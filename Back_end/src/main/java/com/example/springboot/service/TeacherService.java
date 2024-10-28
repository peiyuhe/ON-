package com.example.springboot.service;

import com.example.springboot.model.Teacher;
import com.example.springboot.model.User;
import com.example.springboot.repository.TeacherRepository;
import com.example.springboot.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Teacher createTeacher(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(User.Role.TEACHER);

        Teacher teacher = new Teacher();
        teacher.setUser(user);

        return teacherRepository.save(teacher);
    }

    public Optional<Teacher> getTeacherById(Long teacherId) {
        return teacherRepository.findById(teacherId);
    }


    public void updateTeacher(Long teacherId, String username, String password, String email, String phone, String avatar, String birthDayStr, String genderStr, String facility) {
        Optional<Teacher> teacherOptional = teacherRepository.findById(teacherId);
        if (teacherOptional.isPresent()) {
            Teacher teacher = teacherOptional.get();
            User user = teacher.getUser();

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
                teacher.setFacility(facility);
            }


            userRepository.save(user);
            teacherRepository.save(teacher);
        } else {
            throw new RuntimeException("Teacher not found");
        }
    }

    public void deleteTeacher(Long teacherId) {
        if (teacherRepository.existsById(teacherId)) {
            teacherRepository.deleteById(teacherId);
        } else {
            throw new RuntimeException("teacher not found");
        }
    }
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
}
