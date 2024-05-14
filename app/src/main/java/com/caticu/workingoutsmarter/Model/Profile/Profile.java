package com.caticu.workingoutsmarter.Model.Profile;

public class Profile
{
    public String name;
    public int age;
    public double height;
    public double weight;

    public String Id;

    public Profile() {
        // Default constructor required for Firebase
    }

    public Profile(String name, int age, double height, double weight, String Id) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.Id = Id;
    }
}
