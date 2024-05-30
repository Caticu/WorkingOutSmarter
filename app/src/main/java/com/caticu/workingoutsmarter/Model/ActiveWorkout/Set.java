package com.caticu.workingoutsmarter.Model.ActiveWorkout;

public class Set
{
    private int number;
    private int repetitions;
    private float weight;

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber()
    {
        return number;
    }

    public void setRepetitions(int repetitions)
    {
        this.repetitions = repetitions;
    }
    public int getRepetitions()
    {
        return repetitions;
    }
    public float getWeight()
    {
        return weight;
    }
    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    public Set(int number, int repetitions, float weight)
    {
        this.number = number;
        this.repetitions = repetitions;
        this.weight = weight;
    }
    // No-argument constructor
    public Set() {
    }
}
