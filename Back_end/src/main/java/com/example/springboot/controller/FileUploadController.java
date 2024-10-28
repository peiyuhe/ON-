package com.example.springboot.controller;

import com.example.springboot.dto.SubmissionDTO;
import com.example.springboot.model.*;
import com.example.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private static final String DIRECTORY = Paths.get("src", "main", "resources", "static", "files").toString();
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourseService courseService;

    @GetMapping("/submissions/{exerciseId}")
    public ResponseEntity<List<SubmissionDTO>> getSubmissionsByExerciseId(@PathVariable Long exerciseId) {
        try {
            List<Submission> submissions = submissionService.getSubmissionsByExerciseId(exerciseId);

            List<SubmissionDTO> submissionDTOs = submissions.stream().map(submission -> {
                SubmissionDTO dto = new SubmissionDTO();
                dto.setSubmissionId(submission.getSubmissionId());
                dto.setStudentId(submission.getStudent().getStudentId());
                dto.setFilePath(submission.getFilePath());
                dto.setFeedback(submission.getFeedback());
                dto.setSubmittedAt(submission.getSubmittedAt());
                dto.setGraded(submission.isGraded());
                dto.setScore(submission.getScore());
                dto.setGradedAt(submission.getGradedAt());
                return dto;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(submissionDTOs);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving submissions: " + e.getMessage());
        }
    }
    @GetMapping("/file/{exerciseId}")
    public ResponseEntity<String> getSubmissionUrlByExerciseId(@PathVariable Long exerciseId) {
        try {

            Exercise exercise = exerciseService.getExercise(exerciseId);

            if (exercise == null) {
                return ResponseEntity.badRequest().body("Exercise not found with ID: " + exerciseId);
            }

            String filePath = exercise.getFilePath();
            Path resolvedPath = Paths.get(DIRECTORY).resolve(filePath).normalize();
            Resource resource = new UrlResource(resolvedPath.toUri());

            if (resource.exists()) {
                String fileUrl = resource.getURI().toString();
                return ResponseEntity.ok(fileUrl);
            } else {
                throw new RuntimeException("File not found: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving file: " + e.getMessage());
        }
    }


    @PostMapping("/upload/material")
    public ResponseEntity<String> uploadMaterials(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("courseId") Long courseId,
                                                  @RequestParam("materialType") String materialType) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty");
        }

        try {
            String fileName = file.getOriginalFilename();
            Path materialDirPath = Paths.get(DIRECTORY, "cou" + courseId.toString(), "material");
            Files.createDirectories(materialDirPath); // 创建目录

            Path path = materialDirPath.resolve(fileName);
            Files.write(path, file.getBytes());

            Course course = courseService.getCourse(courseId);
            if (course == null) {
                return ResponseEntity.badRequest().body("Course not found with ID: " + courseId);
            }

            Material material = new Material();
            material.setCourse(course);
            material.setFilePath(path.toString());
            material.setMaterialType(materialType);
            material.setUploadedAt(LocalDateTime.now());

            materialService.createMaterial(material);

            return ResponseEntity.ok("Material uploaded to " + path);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/upload/exercise")
    public ResponseEntity<String> uploadExercises(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("courseId") Long courseId,
                                                  @RequestParam("description") String description,
                                                  @RequestParam("dueDate") String dueDate) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty");
        }

        try {
            String fileName = file.getOriginalFilename();
            Path exerciseDirPath = Paths.get(DIRECTORY, "cou" + courseId.toString(), "exercise");
            Files.createDirectories(exerciseDirPath);

            Path path = exerciseDirPath.resolve(fileName);
            Files.write(path, file.getBytes());

            Course course = courseService.getCourse(courseId);
            if (course == null) {
                return ResponseEntity.badRequest().body("Course not found with ID: " + courseId);
            }

            Exercise exercise = new Exercise();
            exercise.setCourse(course);
            exercise.setFilePath(path.toString());
            exercise.setDescription(description);
            exercise.setDueDate(LocalDateTime.parse(dueDate));
            exercise.setUploadedAt(LocalDateTime.now());

            exerciseService.createExercise(exercise);

            return ResponseEntity.ok("Exercise uploaded to " + path.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PostMapping("/upload/submission")
    public ResponseEntity<String> uploadSubmissions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("exerciseId") Long exerciseId,
            @RequestParam("studentId") Long studentId) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File cannot be empty");
        }

        try {
            String contentType = file.getContentType();
            if (!ALLOWED_TYPES.contains(contentType)) {
                return ResponseEntity.badRequest().body("Only JPG and PNG files are allowed for submissions");
            }

            String fileName = file.getOriginalFilename();
            Path studentDirPath = Paths.get(DIRECTORY, "stu" + studentId.toString());
            Files.createDirectories(studentDirPath);

            Path path = studentDirPath.resolve(fileName);
            Files.write(path, file.getBytes());

            Exercise exercise = exerciseService.getExercise(exerciseId);
            Student student = studentService.getStudent(studentId);
            if (exercise == null || student == null) {
                return ResponseEntity.badRequest().body("Exercise or Student not found");
            }

            Submission submission = new Submission();
            submission.setFilePath(path.toString());
            submission.setExercise(exercise);
            submission.setStudent(student);

            submissionService.createSubmission(submission);

            return ResponseEntity.ok("File uploaded to " + path.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filePath") String filePath) {
        try {
            String decodedPath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
            Path path = Paths.get(DIRECTORY).resolve(decodedPath).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("File not found: " + filePath);
            }

            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            throw new RuntimeException("failed download: " + e.getMessage());
        }
    }


}
