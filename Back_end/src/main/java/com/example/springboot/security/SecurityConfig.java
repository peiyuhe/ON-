package com.example.springboot.security;

import com.example.springboot.filter.JwtAuthenticationFilter;
import com.example.springboot.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil JwtUtil) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        //auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/auth/reset-password").hasAnyAuthority("STUDENT", "TEACHER")
                        //user
                        .requestMatchers("/user/check-security/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/set-security-question").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/update-security-question/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/delete-security-question/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/upload-avatar/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/update-avatar/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/delete-avatar/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/get-avatar/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/download-avatar/**").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/{username}").permitAll()
                        //avatar
                        .requestMatchers("/user/avatar/generate").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/avatar/save").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/avatar/view/{userId}").hasAnyAuthority("STUDENT", "TEACHER")
                        .requestMatchers("/user/avatar/delete/{userId}").hasAnyAuthority("STUDENT", "TEACHER")
                        //student
                        .requestMatchers("/students/delete/{studentId}").hasAuthority("ADMIN")
                        .requestMatchers("/students/update/{studentId}").hasAnyAuthority("STUDENT","ADMIN")
                        .requestMatchers("/students/{studentId}").hasAnyAuthority("STUDENT","ADMIN")
                        .requestMatchers("/students/get/all").hasAuthority("ADMIN")
                        //teacher
                        .requestMatchers("/teachers/create").hasAnyRole("ADMIN", "TEACHER")
                        .requestMatchers("/teachers/{teacherId}").hasAuthority("TEACHER")
                        .requestMatchers("/teachers/update/{teacherId}").hasAuthority("TEACHER")
                        .requestMatchers("/teachers/delete/{teacherId}").hasAuthority("ADMIN")
                        .requestMatchers("/teachers/get/all").hasAuthority("ADMIN")
                        //admin
                        .requestMatchers("/admins/create").permitAll()
                        .requestMatchers("/admins/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/admins/{adminId}").hasAuthority("ADMIN")
                        .requestMatchers("/admins/update/**").hasAuthority("ADMIN")
                        //course
                        .requestMatchers("/courses/create").hasAuthority("TEACHER")
                        .requestMatchers("/courses/update/{courseId}").hasAuthority("TEACHER")
                        .requestMatchers("/courses/delete/{courseId}").hasAuthority("TEACHER")
                        .requestMatchers("/courses/teacher/{teacherId}").hasAuthority("TEACHER")
                        .requestMatchers("/courses/{courseId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/courses/all").hasAnyAuthority("TEACHER", "STUDENT")
                        //enrollment
                        .requestMatchers("/enrollments/create").hasAuthority("STUDENT")
                        .requestMatchers("/enrollments/update/{enrollmentId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/enrollments/student/{studentId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/enrollments/course/{courseId}").hasAnyAuthority( "TEACHER", "STUDENT")
                        .requestMatchers("/enrollments/delete/{enrollmentId}").hasAnyAuthority("STUDENT", "TEACHER")
                        //exercise
                        .requestMatchers("/exercises/create").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/exercises/{exerciseId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/exercises/delete/{exerciseId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/exercises/course/{courseId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/exercises/{exerciseId}/submission").hasAnyAuthority("TEACHER", "STUDENT")
                        //material
                        .requestMatchers("/materials/{materialId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/materials/create").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/update/{materialId}").hasAuthority("TEACHER")
                        .requestMatchers("/materials/delete/{materialId}").hasAuthority("TEACHER")
                        .requestMatchers("/materials/course/{courseId}").hasAnyAuthority("TEACHER", "STUDENT")
                        //submission
                        .requestMatchers("/submission/create").hasAuthority("STUDENT")
                        .requestMatchers("/submission/{submissionId}").hasAuthority("TEACHER")
                        .requestMatchers("/student/{studentId}").hasAuthority("STUDENT")
                        .requestMatchers("/exercise/{exerciseId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/exercise//update/{submissionId}").hasAnyAuthority("TEACHER", "STUDENT")
                        //learning report
                        .requestMatchers("/learning-report/generate-recent-report").hasAuthority("STUDENT")
                        .requestMatchers("/learning-report/generate-by-exercise").hasAuthority("STUDENT")
                        .requestMatchers("/learning-report/generate-by-course").hasAuthority("STUDENT")
                        .requestMatchers("/learning-report/get/{studentId}").hasAuthority("STUDENT")
                        .requestMatchers("/learning-report/delete-report/{studentId}").hasAuthority("STUDENT")
                        .requestMatchers("/learning-report/translate-report").hasAuthority("STUDENT")
                        //forums
                        .requestMatchers("/forums/").hasAnyAuthority("STUDENT","TEACHER")
                        .requestMatchers("/forums/create").hasAnyAuthority("STUDENT","TEACHER")
                        .requestMatchers("/forums/update/{forumId}").hasAnyAuthority("STUDENT","TEACHER")
                        .requestMatchers("/forums/delete/{forumId}").hasAnyAuthority("STUDENT","TEACHER")
                        .requestMatchers("/forums/{forumId}").hasAnyAuthority("STUDENT","TEACHER")
                        .requestMatchers("/forums/student/{studentId}").hasAnyAuthority("STUDENT","TEACHER")
                        .requestMatchers("/forums/student/post-question").hasAuthority("STUDENT")
                        .requestMatchers("/forums/get/all").hasAnyAuthority("STUDENT","TEACHER")
                        //learning plan
                        .requestMatchers("/learningPlans/create").hasAuthority("STUDENT")
                        .requestMatchers("/learningPlans/update/{planId}").hasAuthority("STUDENT")
                        .requestMatchers("/learningPlans/delete/{planId}").hasAuthority("STUDENT")
                        .requestMatchers("/learningPlans/{planId}").hasAuthority("STUDENT")
                        .requestMatchers("/learningPlans/student/{studentId}").hasAuthority("STUDENT")
                        //file_upload
                        .requestMatchers("/files/upload/material").hasAuthority("TEACHER")
                        .requestMatchers("/files/upload/exercise").hasAuthority("TEACHER")
                        .requestMatchers("/files/upload/submission").hasAuthority("STUDENT")
                        .requestMatchers("/files/submissions/{exerciseId}").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/files/download").hasAnyAuthority("TEACHER", "STUDENT")
                        .requestMatchers("/files/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(JwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
