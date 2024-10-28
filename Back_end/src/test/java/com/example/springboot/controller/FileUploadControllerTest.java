package com.example.springboot.controller;

import com.example.springboot.dto.SubmissionDTO;
import com.example.springboot.model.*;
import com.example.springboot.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import static org.hamcrest.Matchers.containsString;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FileUploadControllerTest {
    private static final String DIRECTORY = System.getProperty("user.dir") + "/files";

    private MockMvc mockMvc;

    @Mock
    private SubmissionService submissionService;
    @Mock
    private MaterialService materialService;
    @Mock
    private ExerciseService exerciseService;
    @Mock
    private StudentService studentService;
    @Mock
    private CourseService courseService;

    @InjectMocks
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileUploadController).build();
    }

    @Test
    void testGetSubmissionsByExerciseId() throws Exception {
        // 设置模拟的 Submission 和 Student 实例
        Long exerciseId = 1L;
        Long studentId = 100L;

        // 创建一个 Submission 实例，并设置一个非空的 Student 实例
        Submission submission = new Submission();
        submission.setSubmissionId(1L);
        submission.setFilePath("testFilePath");

        Student student = new Student();
        student.setStudentId(studentId); // 设置 studentId
        submission.setStudent(student); // 设置 student

        // 设置 mock 行为
        when(submissionService.getSubmissionsByExerciseId(exerciseId)).thenReturn(List.of(submission));

        // 执行测试并检查结果
        mockMvc.perform(get("/files/submissions/{exerciseId}", exerciseId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testFilePath")))
                .andExpect(content().string(containsString(studentId.toString())));
    }

    @Test
    void testGetSubmissionUrlByExerciseId() throws Exception {


        Long exerciseId = 1L;
        Path testFilePath = Paths.get(DIRECTORY, "cou" + exerciseId.toString(), "exercise", "testFile.pdf");
        Files.createDirectories(testFilePath.getParent());
        Files.createFile(testFilePath);


        Exercise mockExercise = new Exercise();
        mockExercise.setFilePath(testFilePath.toString());
        when(exerciseService.getExercise(exerciseId)).thenReturn(mockExercise);


        mockMvc.perform(get("/files/file/{exerciseId}", exerciseId))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("testFile.pdf")));


        Files.deleteIfExists(testFilePath);
    }

    @Test
    void testUploadMaterials() throws Exception {
        Course course = new Course();
        course.setCourseId(1L);
        when(courseService.getCourse(1L)).thenReturn(course);

        MockMultipartFile file = new MockMultipartFile("file", "material.pdf", MediaType.APPLICATION_PDF_VALUE, "Test data".getBytes());

        mockMvc.perform(multipart("/files/upload/material")
                        .file(file)
                        .param("courseId", "1")
                        .param("materialType", "PDF"))
                .andExpect(status().isOk())
                .andExpect(content().string("Material uploaded to /files/cou1/material/material.pdf"));
    }

    @Test
    void testUploadExercises() throws Exception {
        Course course = new Course();
        course.setCourseId(1L);
        when(courseService.getCourse(1L)).thenReturn(course);

        MockMultipartFile file = new MockMultipartFile("file", "exercise.pdf", MediaType.APPLICATION_PDF_VALUE, "Test data".getBytes());

        mockMvc.perform(multipart("/files/upload/exercise")
                        .file(file)
                        .param("courseId", "1")
                        .param("description", "Exercise description")
                        .param("dueDate", "2023-12-31T23:59:59"))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise uploaded to /files/cou1/exercise/exercise.pdf"));
    }

    @Test
    void testUploadSubmissions() throws Exception {
        Exercise exercise = new Exercise();
        exercise.setExerciseId(1L);
        Student student = new Student();
        student.setStudentId(1L);
        when(exerciseService.getExercise(1L)).thenReturn(exercise);
        when(studentService.getStudent(1L)).thenReturn(student);

        MockMultipartFile file = new MockMultipartFile("file", "submission.jpg", "image/jpeg", "Test data".getBytes());

        mockMvc.perform(multipart("/files/upload/submission")
                        .file(file)
                        .param("exerciseId", "1")
                        .param("studentId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded to /files/stu1/submission.jpg"));
    }

    @Test
    void testDownloadFile() throws Exception {
        Path filePath = Paths.get(System.getProperty("user.dir") + "/files", "sample.txt");
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, "Sample file content".getBytes());

        mockMvc.perform(get("/files/download").param("filePath", "sample.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"sample.txt\""))
                .andExpect(content().string("Sample file content"));

        Files.deleteIfExists(filePath);
    }
}