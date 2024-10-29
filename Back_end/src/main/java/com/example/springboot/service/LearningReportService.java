package com.example.springboot.service;

import com.example.springboot.dto.LearningReportDTO;
import com.example.springboot.model.*;
import com.example.springboot.repository.*;
import jakarta.transaction.Transactional;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class LearningReportService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private LearningReportsRepository learningReportsRepository;

    @Autowired
    private GenerationKeywordRepository generationKeywordRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Value("${google.api.key}")
    private String apiKey;

    @Value("${baidu.report.api.key}")
    private String API_KEY;

    @Value("${baidu.report.secret.key}")
    private String SECRET_KEY;


    static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();
    public List<LearningReports> getReportsByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return learningReportsRepository.findAllByStudent(student);
    }


    @Transactional
    public LearningReportDTO generateLearningReport(Long studentId) throws IOException {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<Submission> submissions = submissionRepository.findTop4ByStudentOrderBySubmittedAtDesc(student);


        if (submissions.size() < 4) {
            throw new IllegalArgumentException("Not enough submissions to generate a complete learning report (minimum 4 required).");
        }

        generationKeywordRepository.deleteByStudent(student);

        for (Submission submission : submissions) {
            GenerationKeyword keyword = new GenerationKeyword();
            keyword.setStudent(student);
            keyword.setScore(submission.getScore());
            Exercise exercise = submission.getExercise();
            Course course = exercise.getCourse();
            keyword.setCourse(course);
            keyword.setExercise(exercise);
            keyword.setDescription(submission.getExercise().getDescription());
            keyword.setFeedback(submission.getFeedback());
            generationKeywordRepository.save(keyword);
        }

        String aiGeneratedReport = generateReportFromSubmissions(submissions);

        LearningReports learningReport = new LearningReports();
        learningReport.setStudent(student);
        learningReport.setReportData(aiGeneratedReport);
        learningReport.setGeneratedAt(LocalDateTime.now());
        learningReportsRepository.save(learningReport);

//        return aiGeneratedReport;
        return new LearningReportDTO(learningReport.getReportId(), aiGeneratedReport);
    }

    @Transactional
    public LearningReportDTO generateCourseLearningReport(Long studentId, Long courseId) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        List<Submission> latestSubmissions = submissionRepository.findLatestSubmissionsByStudentAndCourse(studentId, courseId);

        if (latestSubmissions.isEmpty()) {
            throw new IllegalArgumentException("No submissions found for the specified course.");
        }
        Map<Exercise, Submission> latestSubmissionsMap = new HashMap<>();
        for (Submission submission : latestSubmissions) {
            Exercise exercise = submission.getExercise();
            if (!latestSubmissionsMap.containsKey(exercise) ||
                    submission.getSubmittedAt().isAfter(latestSubmissionsMap.get(exercise).getSubmittedAt())) {
                latestSubmissionsMap.put(exercise, submission);
            }
        }

        generationKeywordRepository.deleteByStudent(student);

        for (Submission submission : latestSubmissionsMap.values()) {
            GenerationKeyword keyword = new GenerationKeyword();
            keyword.setStudent(student);
            keyword.setScore(submission.getScore());
            keyword.setExercise(submission.getExercise());
            keyword.setCourse(submission.getExercise().getCourse());
            keyword.setDescription(submission.getExercise().getDescription());
            keyword.setFeedback(submission.getFeedback());
            generationKeywordRepository.save(keyword);
        }

        String aiGeneratedReport = generateReportFromSubmissions(latestSubmissions);

        LearningReports learningReport = new LearningReports();
        learningReport.setStudent(student);
        learningReport.setReportData(aiGeneratedReport);
        learningReport.setGeneratedAt(LocalDateTime.now());
        learningReportsRepository.save(learningReport);

//        return aiGeneratedReport;
        return new LearningReportDTO(learningReport.getReportId(), aiGeneratedReport);
    }

    @Transactional
    public LearningReportDTO generateExerciseLearningReport(Long studentId, Long exerciseId) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        List<Submission> submissions = submissionRepository.findByStudentAndExerciseOrderBySubmittedAtDesc(student, exercise);

        if (submissions.isEmpty()) {
            throw new IllegalArgumentException("No submissions found for the specified exercise.");
        }

        generationKeywordRepository.deleteByStudent(student);

        for (Submission submission : submissions) {
            GenerationKeyword keyword = new GenerationKeyword();
            keyword.setStudent(student);
            keyword.setScore(submission.getScore());
            keyword.setExercise(submission.getExercise());
            keyword.setCourse(submission.getExercise().getCourse());
            keyword.setDescription(submission.getExercise().getDescription());
            keyword.setFeedback(submission.getFeedback());
            generationKeywordRepository.save(keyword);
        }
        String aiGeneratedReport = generateReportForMultipleSubmissions(submissions, exercise);

        LearningReports learningReport = new LearningReports();
        learningReport.setStudent(student);
        learningReport.setReportData(aiGeneratedReport);
        learningReport.setGeneratedAt(LocalDateTime.now());
        learningReportsRepository.save(learningReport);

//        return aiGeneratedReport;
        return new LearningReportDTO(learningReport.getReportId(), aiGeneratedReport);
    }

    private String generateReportFromSubmissions(List<Submission> submissions) throws IOException {
        StringBuilder messageContent = new StringBuilder();
        messageContent.append("Please generate a formal learning report based on the following information, and review the related course test content to provide more specific study suggestions:\n");

        for (Submission submission : submissions) {
            String courseName = submission.getExercise().getCourse().getCourseName();
            int score = submission.getScore();
            String description = submission.getExercise().getDescription();

            messageContent.append(String.format("In the course \"%s\", I scored %d points. The test content mainly covered %s.\n", courseName, score, description));
        }

        messageContent.append("Report Requirements:\n");
        messageContent.append("Review the test scope for each course and summarize the related content.\n");
        messageContent.append("Based on the specific knowledge points of these courses, provide more detailed study suggestions.\n");

        return callAIService(messageContent.toString());
    }

    private String generateReportForMultipleSubmissions(List<Submission> submissions, Exercise exercise) throws IOException {
        StringBuilder messageContent = new StringBuilder();
        messageContent.append("Please generate a detailed learning report based on the following multiple submissions by the student for the same exercise.\n");
        messageContent.append("The report should analyze the student's progress over time, compare different submission scores, and provide specific suggestions for improvement.\n");

        String courseName = exercise.getCourse().getCourseName();
        String description = exercise.getDescription();
        messageContent.append(String.format("The exercise \"%s\" mainly covered the following topics: %s.\n", courseName, description));

        for (Submission submission : submissions) {
            int score = submission.getScore();
            LocalDateTime submittedAt = submission.getSubmittedAt();
            messageContent.append(String.format("Submission on %s: The student scored %d points.\n", submittedAt.toString(), score));
        }

        messageContent.append("Please analyze the above submission history, highlight the student's progress, and suggest areas for further improvement based on the test content.\n");

        return callAIService(messageContent.toString());
    }

    private String callAIService(String prompt) throws IOException {
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.put("temperature", 0.95);
        requestBodyJson.put("top_p", 0.8);
        requestBodyJson.put("penalty_score", 1);
        requestBodyJson.put("enable_system_memory", false);
        requestBodyJson.put("disable_search", false);
        requestBodyJson.put("enable_citation", false);
        requestBodyJson.put("response_format", "text");

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);

        requestBodyJson.put("messages", new org.json.JSONArray().put(message));

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBodyJson.toString());

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions?access_token=" + getAccessToken())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = HTTP_CLIENT.newCall(request).execute();
        String responseBodyString = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBodyString);
        if (jsonResponse.has("result")) {
            return jsonResponse.getString("result");
        } else {
            return "AI 无法生成报告。";
        }
    }

    private String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);

        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        Response response = HTTP_CLIENT.newCall(request).execute();
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String responseBodyString = responseBody.string();
            JSONObject jsonResponse = new JSONObject(responseBodyString);
            return jsonResponse.getString("access_token");
        } else {
            throw new IOException("无法获取 Access Token");
        }
    }

    public LearningReports getReportByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return learningReportsRepository.findByStudent(student).orElse(null);
    }
    public LearningReports getReportById(Long reportId) {
        return learningReportsRepository.findById(reportId).orElse(null);
    }

    public boolean deleteReportByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Optional<LearningReports> reportOptional = learningReportsRepository.findByStudent(student);
        if (reportOptional.isPresent()) {
            learningReportsRepository.deleteByStudent(student);
            return true;
        } else {
            return false;
        }
    }
    public String translateReportUsingApiKey(String reportData, String targetLanguage) {
        String url = "https://translation.googleapis.com/language/translate/v2";

        try {
            String encodedReportData = reportData;

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("q", encodedReportData)
                    .queryParam("target", targetLanguage)
                    .queryParam("key", apiKey);

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(builder.toUriString(), Map.class);

            if (response != null && response.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) response.get("data");
                List<Map<String, String>> translations = (List<Map<String, String>>) data.get("translations");
                if (!translations.isEmpty()) {
                    String translatedText = translations.get(0).get("translatedText");
                    translatedText = translatedText.replaceAll("%[0-9A-Fa-f]{2}", " ");
                    return translatedText;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Translation failed";
    }

}
