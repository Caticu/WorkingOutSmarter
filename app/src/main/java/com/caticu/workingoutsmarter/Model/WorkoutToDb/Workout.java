package com.caticu.workingoutsmarter.Model.WorkoutToDb;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workout implements Serializable {
    private String name;
    private List<Set> sets;
    private String date;

    public Workout() {}

    public Workout(String name, List<Set> sets, String date) {
        this.name = name;
        this.sets = sets;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}