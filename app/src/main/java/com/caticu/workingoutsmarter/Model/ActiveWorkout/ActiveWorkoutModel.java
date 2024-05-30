package com.caticu.workingoutsmarter.Model.ActiveWorkout;

import com.caticu.workingoutsmarter.API.WorkoutFromAPI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActiveWorkoutModel implements Serializable {
    private String id;
    private String name;
    public String getName(){return name;}
    public void setName(){this.name = name;}
    private String equipment;
    private String instructions;
    private List<Set> sets;

    // Getters and setters
    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getId() {
        return id;
    }
    public List<Set> getSets() {
        return sets;
    }
    public void setSets(Set set) {
        sets.add(set);
    }

    public ActiveWorkoutModel(String name, String equipment, String instructions, List<Set> sets )
    {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.equipment = equipment;
        this.instructions = instructions;
        this.sets = sets;
    }


    public static ActiveWorkoutModel convertToActiveWorkoutModel(WorkoutFromAPI workoutFromAPI) {
        return new ActiveWorkoutModel(
                workoutFromAPI.getName(),
                workoutFromAPI.getEquipment(),
                workoutFromAPI.getInstructions(),
                new ArrayList<>()
        );
    }


}
