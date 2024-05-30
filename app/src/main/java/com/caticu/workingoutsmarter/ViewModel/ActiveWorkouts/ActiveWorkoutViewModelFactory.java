// ActiveWorkoutViewModelFactory.java
package com.caticu.workingoutsmarter.ViewModel.ActiveWorkouts;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.caticu.workingoutsmarter.Repository.IWorkoutRepository;

public class ActiveWorkoutViewModelFactory implements ViewModelProvider.Factory {
    private final IWorkoutRepository workoutRepository;

    public ActiveWorkoutViewModelFactory(IWorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ActiveWorkoutViewModel.class)) {
            return (T) new ActiveWorkoutViewModel(workoutRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
