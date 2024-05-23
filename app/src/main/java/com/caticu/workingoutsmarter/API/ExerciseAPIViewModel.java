package com.caticu.workingoutsmarter.API;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.API.WorkoutFromAPI;
import com.caticu.workingoutsmarter.API.ExerciseRepository;

import java.util.List;

public class ExerciseAPIViewModel extends ViewModel {
    private final ExerciseRepository repository;

    public ExerciseAPIViewModel() {
        repository = ExerciseRepository.getInstance();
    }

    public LiveData<List<WorkoutFromAPI>> searchExercises(String muscle) {
        return repository.searchExercises(muscle);
    }
}