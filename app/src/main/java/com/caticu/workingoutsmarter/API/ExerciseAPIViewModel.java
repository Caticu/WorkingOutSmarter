package com.caticu.workingoutsmarter.API;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ExerciseAPIViewModel extends ViewModel {
    private final ExerciseRepository repository;
    private final MutableLiveData<List<WorkoutFromAPI>> exercises = new MutableLiveData<>();

    public ExerciseAPIViewModel() {
        repository = ExerciseRepository.getInstance();
    }

    public LiveData<List<WorkoutFromAPI>> searchExercises(String muscle)
    {
        return repository.searchExercises(muscle);
    }

    public LiveData<List<WorkoutFromAPI>> getExercises() {

        return exercises;
    }

    public void setExercises(List<WorkoutFromAPI> exerciseList)
    {
        exercises.setValue(exerciseList);
    }
}
