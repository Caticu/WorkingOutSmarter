package com.caticu.workingoutsmarter.Model.Profile;

public class Profile {
    public String name;
    public int age;
    public double height;
    public double weight;
    public String Id;
    public String imageUri;

    // empty contrsc for firebase
    public Profile() {
    }

    public Profile(String name, int age, double height, double weight, String Id, String imageUri) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.Id = Id;
        this.imageUri = imageUri;
    }
}
