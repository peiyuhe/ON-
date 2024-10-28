package com.example.springboot.service;

import com.example.springboot.model.Exercise;
import com.example.springboot.repository.ExerciseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;


    public Exercise createExercise(Exercise exercise) {

        return exerciseRepository.save(exercise);
    }

    @Transactional
    public Exercise getExercise(Long exerciseId) {return exerciseRepository.findById(exerciseId).orElseThrow();}


    @Transactional
    public Exercise updateExercise(Long exerciseId, Exercise exerciseDetail) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow();

        exercise.setDueDate(exerciseDetail.getDueDate());
        exercise.setFilePath(exerciseDetail.getFilePath());
        exercise.setDescription(exerciseDetail.getDescription());

        return exerciseRepository.save(exercise);
    }

    @Transactional
    public void deleteExercise(Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).orElseThrow();
        exerciseRepository.delete(exercise);
    }
    public List<Exercise> getExercisesByCourseId(Long courseId) {
        return exerciseRepository.findByCourseCourseId(courseId);
    }

}
