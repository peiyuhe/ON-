package com.example.springboot.dto;

public class EnrollmentDTO {
    private Long enrollmentId;
    private Long courseId;
    private Long studentId;

    // Constructors
    public EnrollmentDTO(Long enrollmentId, Long courseId, Long studentId) {
        this.enrollmentId = enrollmentId;
        this.courseId = courseId;
        this.studentId = studentId;
    }

    // Getters and Setters
    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}