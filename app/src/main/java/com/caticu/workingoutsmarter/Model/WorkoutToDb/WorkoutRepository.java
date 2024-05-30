package com.caticu.workingoutsmarter.Model.WorkoutToDb;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WorkoutRepository {
    private static WorkoutRepository instance;
    private MutableLiveData<List<Workout>> workouts;

    private WorkoutRepository() {
        workouts = new MutableLiveData<>();
    }

    public static synchronized WorkoutRepository getInstance() {
        if (instance == null) {
            instance = new WorkoutRepository();
        }
        return instance;
    }

}