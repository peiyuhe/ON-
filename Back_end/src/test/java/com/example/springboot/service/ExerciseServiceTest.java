package com.example.springboot.service;

import com.example.springboot.model.Exercise;
import com.example.springboot.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExercise() {
        Exercise exercise = new Exercise();
        exercise.setDescription("Math Homework");

        when(exerciseRepository.save(exercise)).thenReturn(exercise);

        Exercise createdExercise = exerciseService.createExercise(exercise);

        assertNotNull(createdExercise);
        assertEquals("Math Homework", createdExercise.getDescription());
        verify(exerciseRepository, times(1)).save(exercise);
    }

    @Test
    void testGetExercise_whenExerciseExists() {
        Exercise exercise = new Exercise();
        exercise.setExerciseId(1L);
        exercise.setDescription("Math Homework");

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        Exercise foundExercise = exerciseService.getExercise(1L);

        assertNotNull(foundExercise);
        assertEquals(1L, foundExercise.getExerciseId());
        assertEquals("Math Homework", foundExercise.getDescription());
        verify(exerciseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetExercise_whenExerciseDoesNotExist() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.getExercise(1L));
    }

    @Test
    void testUpdateExercise_whenExerciseExists() {
        Exercise existingExercise = new Exercise();
        existingExercise.setExerciseId(1L);
        existingExercise.setDescription("Math Homework");

        Exercise exerciseDetails = new Exercise();
        exerciseDetails.setDescription("Updated Homework");
        exerciseDetails.setFilePath("/path/to/file");
        exerciseDetails.setDueDate(LocalDateTime.parse("2024-12-01T00:00")); // 更新日期格式

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(existingExercise)).thenReturn(existingExercise);

        Exercise updatedExercise = exerciseService.updateExercise(1L, exerciseDetails);

        assertNotNull(updatedExercise);
        assertEquals("Updated Homework", updatedExercise.getDescription());
        assertEquals("/path/to/file", updatedExercise.getFilePath());
        assertEquals(LocalDateTime.parse("2024-12-01T00:00"), updatedExercise.getDueDate()); // 验证格式
        verify(exerciseRepository, times(1)).save(existingExercise);
    }

    @Test
    void testUpdateExercise_whenExerciseDoesNotExist() {
        Exercise exerciseDetails = new Exercise();
        exerciseDetails.setDescription("Updated Homework");

        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.updateExercise(1L, exerciseDetails));
    }

    @Test
    void testDeleteExercise_whenExerciseExists() {
        Exercise exercise = new Exercise();
        exercise.setExerciseId(1L);

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));

        exerciseService.deleteExercise(1L);

        verify(exerciseRepository, times(1)).delete(exercise);
    }

    @Test
    void testDeleteExercise_whenExerciseDoesNotExist() {
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> exerciseService.deleteExercise(1L));
    }

    @Test
    void testGetExercisesByCourseId() {
        Long courseId = 1L;
        Exercise exercise1 = new Exercise();
        exercise1.setExerciseId(1L);
        Exercise exercise2 = new Exercise();
        exercise2.setExerciseId(2L);

        when(exerciseRepository.findByCourseCourseId(courseId)).thenReturn(List.of(exercise1, exercise2));

        List<Exercise> exercises = exerciseService.getExercisesByCourseId(courseId);

        assertEquals(2, exercises.size());
        assertEquals(1L, exercises.get(0).getExerciseId());
        assertEquals(2L, exercises.get(1).getExerciseId());
        verify(exerciseRepository, times(1)).findByCourseCourseId(courseId);
    }
}