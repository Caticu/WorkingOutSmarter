package com.caticu.workingoutsmarter.View.Fragments;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.caticu.workingoutsmarter.Model.Profile.Profile;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.ViewModel.Profile.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editName;
    private EditText editAge;
    private EditText editHeight;
    private EditText editWeight;
    private Button btnSave;
    private ImageButton btnSelectImage;
    private ImageView profileImageView;
    private ProgressBar progressBar;
    private ProfileViewModel profileViewModel;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private StorageReference storageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().setTitle("Profile");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ProfileImages");

        editName = view.findViewById(R.id.editTextName);
        editAge = view.findViewById(R.id.AgeEditText);
        editHeight = view.findViewById(R.id.HeightEditText);
        editWeight = view.findViewById(R.id.WeightEditText);
        btnSave = view.findViewById(R.id.SaveButton);
        btnSelectImage = view.findViewById(R.id.changePictureButton);
        profileImageView = view.findViewById(R.id.profileImage);
        progressBar = view.findViewById(R.id.progressBar);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Observe the profile data
        profileViewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                editName.setText(profile.name);
                editAge.setText(String.valueOf(profile.age));
                editHeight.setText(String.valueOf(profile.height));
                editWeight.setText(String.valueOf(profile.weight));
                if (profile.imageUri != null) {
                    Picasso.get().load(profile.imageUri).into(profileImageView);
                }
                progressBar.setVisibility(View.GONE);  // Hide the progress bar when data is loaded
            } else {
                Toast.makeText(getActivity(), "Profile data not found.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);  // Hide the progress bar if data is not found
            }
        });

        // Fetch profile data from Firebase if not already loaded
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && profileViewModel.getProfileData().getValue() == null) {
            progressBar.setVisibility(View.VISIBLE);  // Show the progress bar
            String userId = currentUser.getUid();
            profileViewModel.fetchProfile(userId);
        }

        btnSelectImage.setOnClickListener(v -> openFileChooser());

        btnSave.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);  // Show the progress bar when saving profile
            saveProfile();
        });

        profileViewModel.getSaveSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                Toast.makeText(getActivity(), "Profile saved successfully!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);  // Hide the progress bar on success
            }
        });

        profileViewModel.getSaveError().observe(getViewLifecycleOwner(), error -> {
            if (error) {
                Toast.makeText(getActivity(), "Error saving profile or no data was added!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);  // Hide the progress bar on error
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void saveProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String name = editName.getText().toString();
            int age = Integer.parseInt(editAge.getText().toString());
            double height = Double.parseDouble(editHeight.getText().toString());
            double weight = Double.parseDouble(editWeight.getText().toString());

            if (imageUri != null) {
                StorageReference fileReference = storageReference.child(userId + "/" + UUID.randomUUID().toString());
                fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            profileViewModel.saveProfile(userId, name, age, height, weight, uri.toString());
                        }).addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);  // Hide the progress bar on failure
                        })).addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);  // Hide the progress bar on failure
                });
            } else {
                profileViewModel.saveProfile(userId, name, age, height, weight, null);
            }
        } else {
            Toast.makeText(getActivity(), "User not authenticated!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);  // Hide the progress bar if user is not authenticated
        }
    }
}