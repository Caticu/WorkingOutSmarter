// WorkoutRepository.java
package com.caticu.workingoutsmarter.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.caticu.workingoutsmarter.Model.WorkoutToDb.Workout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutRepository implements IWorkoutRepository {
    private static WorkoutRepository instance;
    private final FirebaseDatabase database;
    private final DatabaseReference profileRef;

    private WorkoutRepository() {
        database = FirebaseDatabase.getInstance("https://workoutpal-c9759-default-rtdb.europe-west1.firebasedatabase.app/");
        profileRef = database.getReference("Workouts");
    }

    public static synchronized WorkoutRepository getInstance() {
        if (instance == null) {
            instance = new WorkoutRepository();
        }
        return instance;
    }

    @Override
    public void saveWorkouts(String userId, List<Workout> workouts) {
        for (Workout workout : workouts) {
            String key = profileRef.child(userId).push().getKey(); // Generate a unique push ID
            Map<String, Object> workoutDetails = new HashMap<>();
            workoutDetails.put("name", workout.getName());
            workoutDetails.put("date", workout.getDate());

            List<Map<String, Object>> sets = new ArrayList<>();
            for (Set set : workout.getSets()) {
                Map<String, Object> setData = new HashMap<>();
                setData.put("number", set.getNumber());
                setData.put("repetitions", set.getRepetitions());
                setData.put("weight", set.getWeight());
                sets.add(setData);
            }
            workoutDetails.put("sets", sets);

            // Save workout using the generated push ID
            if (key != null) {
                profileRef.child(userId).child(key).setValue(workoutDetails);
            }
        }
    }

    @Override
    public LiveData<List<Set>> getSetsForWorkoutName(String workoutName, String userId) {
        MutableLiveData<List<Set>> setsLiveData = new MutableLiveData<>();
        Query query = profileRef.child(userId).orderByChild("name").equalTo(workoutName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Set> sets = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Workout workout = snapshot.getValue(Workout.class);
                    if (workout != null && workout.getSets() != null) {
                        sets.addAll(workout.getSets());
                    }
                }
                setsLiveData.setValue(sets);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                setsLiveData.setValue(null);
            }
        });
        return setsLiveData;
    }

    @Override
    public LiveData<List<String>> getWorkoutDates(String userId) {
        MutableLiveData<List<String>> datesLiveData = new MutableLiveData<>();
        profileRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> dates = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Workout workout = snapshot.getValue(Workout.class);
                    if (workout != null && workout.getDate() != null && !dates.contains(workout.getDate())) {
                        dates.add(workout.getDate());
                    }
                }
                datesLiveData.setValue(dates);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                datesLiveData.setValue(null);
            }
        });
        return datesLiveData;
    }

    @Override
    public LiveData<List<Workout>> getWorkoutsForDate(String date, String userId) {
        MutableLiveData<List<Workout>> workoutsLiveData = new MutableLiveData<>();
        Query query = profileRef.child(userId).orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Workout> workouts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Workout workout = snapshot.getValue(Workout.class);
                    if (workout != null) {
                        workouts.add(workout);
                    }
                }
                workoutsLiveData.setValue(workouts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                workoutsLiveData.setValue(null);
            }
        });
        return workoutsLiveData;
    }

}
