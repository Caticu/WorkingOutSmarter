// ActiveWorkoutViewModel.java
package com.caticu.workingoutsmarter.ViewModel.ActiveWorkouts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel;
import com.caticu.workingoutsmarter.Model.WorkoutToDb.Workout;
import com.caticu.workingoutsmarter.Repository.IWorkoutRepository;

import java.util.ArrayList;
import java.util.List;

public class ActiveWorkoutViewModel extends ViewModel {

    private IWorkoutRepository workoutRepository;
    private LiveData<List<Workout>> workouts;
    private final MutableLiveData<List<ActiveWorkoutModel>> workoutList = new MutableLiveData<>(new ArrayList<>());

    public ActiveWorkoutViewModel(IWorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
        workouts = new MutableLiveData<>(); // Initialize if necessary
    }

    public LiveData<List<Workout>> getWorkouts() {
        return workouts;
    }

    public void saveWorkouts(String userId, List<Workout> workoutsList) {
        workoutRepository.saveWorkouts(userId, workoutsList);
    }



    public LiveData<List<ActiveWorkoutModel>> getActiveWorkouts() {
        return workoutList;
    }

    public void addWorkout(ActiveWorkoutModel workout) {
        List<ActiveWorkoutModel> currentList = workoutList.getValue();
        if (currentList != null) {
            currentList.add(workout);
            workoutList.setValue(currentList);
        }
    }

    public void removeWorkout(ActiveWorkoutModel workout) {
        List<ActiveWorkoutModel> currentList = workoutList.getValue();
        if (currentList != null) {
            currentList.remove(workout);
            workoutList.setValue(currentList);
        }
    }

    public void clearActiveWorkouts()
    {
        workoutList.setValue(new ArrayList<>());
    }
}
