package com.caticu.workingoutsmarter;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.caticu.workingoutsmarter.ViewModel.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editAge;
    private EditText editHeight;
    private EditText editWeight;
    private Button btnSave;
    private ProfileViewModel profileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editName = findViewById(R.id.editTextName);
        editAge = findViewById(R.id.AgeEditText);
        editHeight = findViewById(R.id.HeightEditText);
        editWeight = findViewById(R.id.WeightEditText);
        btnSave = findViewById(R.id.SaveButton);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        btnSave.setOnClickListener(view -> saveProfile());

    }

    private void saveProfile()
    {
        String name = editName.getText().toString();
        int age = Integer.parseInt(editAge.getText().toString());
        double height = Double.parseDouble(editHeight.getText().toString());
        double weight = Double.parseDouble(editWeight.getText().toString());

        profileViewModel.saveProfile(name, age, height, weight);

        // Observe save success and error here and update UI accordingly
        profileViewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                // Handle successful save
            }
        });

        profileViewModel.getSaveError().observe(this, error -> {
            if (error) {
                // Handle save error
            }
        });
    }
}

