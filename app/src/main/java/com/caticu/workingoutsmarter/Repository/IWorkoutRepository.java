package com.caticu.workingoutsmarter.Repository;

import androidx.lifecycle.LiveData;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.caticu.workingoutsmarter.Model.WorkoutToDb.Workout;

import java.util.List;

public interface IWorkoutRepository
{
    void saveWorkouts(String userId, List<Workout> workouts);
    LiveData<List<Set>> getSetsForWorkoutName(String workoutName, String userId);
    LiveData<List<String>> getWorkoutDates(String userId);
    LiveData<List<Workout>> getWorkoutsForDate(String date, String userId);

}
