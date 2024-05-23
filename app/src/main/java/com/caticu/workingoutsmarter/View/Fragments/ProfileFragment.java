package com.caticu.workingoutsmarter.View.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.caticu.workingoutsmarter.Model.Profile.Profile;
import com.caticu.workingoutsmarter.Model.Profile.ProfileModel;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.ViewModel.Profile.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment
{

    private EditText editName;
    private EditText editAge;
    private EditText editHeight;
    private EditText editWeight;
    private Button btnSave;
    private ProfileViewModel profileViewModel;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        editName = view.findViewById(R.id.editTextName);
        editAge = view.findViewById(R.id.AgeEditText);
        editHeight = view.findViewById(R.id.HeightEditText);
        editWeight = view.findViewById(R.id.WeightEditText);
        btnSave = view.findViewById(R.id.SaveButton);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Get profile data from Firebase and populate the views
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            ProfileModel profileModel = new ProfileModel();
            profileModel.getProfile(userId, new ProfileModel.OnProfileDataCallback() {
                @Override
                public void onProfileDataLoaded(Profile profile) {
                    if (profile != null) {
                        editName.setText(profile.name);
                        editAge.setText(String.valueOf(profile.age));
                        editHeight.setText(String.valueOf(profile.height));
                        editWeight.setText(String.valueOf(profile.weight));
                    } else {
                        Toast.makeText(getActivity(), "Profile data not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProfileDataNotAvailable(String errorMessage) {
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }

        btnSave.setOnClickListener(view1 -> saveProfile());

        profileViewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getActivity(), "Profile saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        profileViewModel.getSaveError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Toast.makeText(getActivity(), "Error saving profile!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String name = editName.getText().toString();
        int age = Integer.parseInt(editAge.getText().toString());
        double height = Double.parseDouble(editHeight.getText().toString());
        double weight = Double.parseDouble(editWeight.getText().toString());
        String userId = "";
        if (currentUser != null) {
            userId = currentUser.getUid();
            profileViewModel.saveProfile(userId, name, age, height, weight);
        } else {
            Toast.makeText(getActivity(), "User not authenticated!", Toast.LENGTH_SHORT).show();
        }
        profileViewModel.saveProfile(userId, name, age, height, weight);
    }
}
