package com.caticu.workingoutsmarter.Model.Profile;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileModel
{
    private DatabaseReference profileRef;

    public ProfileModel()
    {
        // Initialize Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://workoutpal-c9759-default-rtdb.europe-west1.firebasedatabase.app/");
        profileRef = database.getReference("Profiles");
    }

    public void saveProfile(String userId, String name, int age, double height, double weight, OnProfileSaveCallback callback)
    {
        Profile profile = new Profile(name, age, height, weight, userId);
        profileRef.child(userId).setValue(profile)
                .addOnSuccessListener(aVoid -> callback.onProfileSaveSuccess())
                .addOnFailureListener(e -> callback.onProfileSaveFailure(e));
    }

    public void getProfile(String userId, OnProfileDataCallback callback)
    {
        profileRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Profile profile = dataSnapshot.getValue(Profile.class);
                    callback.onProfileDataLoaded(profile);
                } else
                {
                    callback.onProfileDataNotAvailable("Profile data not found.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                callback.onProfileDataNotAvailable(databaseError.getMessage());
            }
        });
    }

    public interface OnProfileSaveCallback
    {
        void onProfileSaveSuccess();
        void onProfileSaveFailure(Exception e);
    }
    public interface OnProfileDataCallback
    {
        void onProfileDataLoaded(Profile profile);
        void onProfileDataNotAvailable(String errorMessage);
    }
}
