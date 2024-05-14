package com.caticu.workingoutsmarter.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.caticu.workingoutsmarter.Model.Profile.Profile;
import com.caticu.workingoutsmarter.Model.Profile.ProfileModel;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.ViewModel.Profile.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editAge;
    private EditText editHeight;
    private EditText editWeight;
    private Button btnSave;
    private ProfileViewModel profileViewModel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        editName = findViewById(R.id.editTextName);
        editAge = findViewById(R.id.AgeEditText);
        editHeight = findViewById(R.id.HeightEditText);
        editWeight = findViewById(R.id.WeightEditText);
        btnSave = findViewById(R.id.SaveButton);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Get profile data from Firebase and populate the views
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            ProfileModel profileModel = new ProfileModel();
            profileModel.getProfile(userId, new ProfileModel.OnProfileDataCallback()
            {
                @Override
                public void onProfileDataLoaded(Profile profile)
                {
                    if (profile != null)
                    {
                        editName.setText(profile.name);
                        editAge.setText(String.valueOf(profile.age));
                        editHeight.setText(String.valueOf(profile.height));
                        editWeight.setText(String.valueOf(profile.weight));
                    }
                    else
                    {
                        Toast.makeText(ProfileActivity.this, "Profile data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProfileDataNotAvailable(String errorMessage)
                {
                    Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnSave.setOnClickListener(view -> saveProfile());

        profileViewModel.getSaveSuccess().observe(this, success ->
        {
            if (success)
            {
                Toast.makeText(ProfileActivity.this, "Profile saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getSaveError().observe(this, error ->
        {
            if (error)
            {
                Toast.makeText(ProfileActivity.this, "Error saving profile!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void saveProfile()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String name = editName.getText().toString();
        int age = Integer.parseInt(editAge.getText().toString());
        double height = Double.parseDouble(editHeight.getText().toString());
        double weight = Double.parseDouble(editWeight.getText().toString());
        String userId = "";
        if (currentUser != null)
        {
            userId = currentUser.getUid();
            profileViewModel.saveProfile(userId, name, age, height, weight);
        }
        else
        {
            Toast.makeText(ProfileActivity.this, "User not authenticated!", Toast.LENGTH_SHORT).show();
        }
        profileViewModel.saveProfile(userId, name, age, height, weight);
    }
}

