package com.caticu.workingoutsmarter.ViewModel.ActiveWorkoutsDetail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel;
import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;

import java.util.ArrayList;
import java.util.List;

public class ActiveWorkoutsDetailViewModel extends ViewModel {
    private final MutableLiveData<ActiveWorkoutModel> workout = new MutableLiveData<>();
    private final MutableLiveData<Integer> setCount = new MutableLiveData<>(0);

    public LiveData<ActiveWorkoutModel> getWorkout() {
        return workout;
    }

    public void setWorkout(ActiveWorkoutModel workout) {
        this.workout.setValue(workout);
    }

    public LiveData<Integer> getSetCount() {
        return setCount;
    }

    public void incrementSetCount() {
        setCount.setValue(setCount.getValue() + 1);
    }

    public void addSet(Set set) {
        ActiveWorkoutModel currentWorkout = workout.getValue();
        if (currentWorkout != null) {
            currentWorkout.getSets().add(set);
            workout.setValue(currentWorkout);
        }
    }
}
