package com.caticu.workingoutsmarter.Model.Profile;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileModel {
    private DatabaseReference profileRef;

    public ProfileModel()
    {
        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        profileRef = database.getReference("profiles");
    }

    public void saveProfile(String userId, String name, int age, double height, double weight)
    {
        // Assuming userId is used as the key for the profile data
        profileRef.child(userId).setValue(new Profile(name, age, height, weight));
    }
}
