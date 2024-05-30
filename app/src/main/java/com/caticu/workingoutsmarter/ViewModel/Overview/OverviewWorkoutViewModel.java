package com.caticu.workingoutsmarter.ViewModel.Overview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.caticu.workingoutsmarter.Model.WorkoutToDb.Workout;
import com.caticu.workingoutsmarter.Repository.IWorkoutRepository;

import java.util.List;

public class OverviewWorkoutViewModel extends ViewModel {

    private final IWorkoutRepository workoutRepository;
    private final MutableLiveData<String> selectedDate = new MutableLiveData<>();

    public OverviewWorkoutViewModel(IWorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public LiveData<List<Set>> getSetsForWorkoutName(String workoutName, String userId) {
        return workoutRepository.getSetsForWorkoutName(workoutName, userId);
    }

    public LiveData<List<String>> getWorkoutDates(String userId) {
        return workoutRepository.getWorkoutDates(userId);
    }

    public LiveData<List<Workout>> getWorkoutsForDate(String date, String userId) {
        return workoutRepository.getWorkoutsForDate(date, userId);
    }

    public void setSelectedDate(String date) {
        selectedDate.setValue(date);
    }

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }
}
