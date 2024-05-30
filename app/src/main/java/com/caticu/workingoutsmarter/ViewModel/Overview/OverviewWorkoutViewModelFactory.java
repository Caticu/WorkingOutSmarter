package com.caticu.workingoutsmarter.ViewModel.Overview;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.caticu.workingoutsmarter.Repository.IWorkoutRepository;

public class OverviewWorkoutViewModelFactory implements ViewModelProvider.Factory {

    private final IWorkoutRepository workoutRepository;

    public OverviewWorkoutViewModelFactory(IWorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OverviewWorkoutViewModel.class)) {
            return (T) new OverviewWorkoutViewModel(workoutRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
