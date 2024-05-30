package com.caticu.workingoutsmarter.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.caticu.workingoutsmarter.MainActivity;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.View.Activities.LoginActivity;
import com.caticu.workingoutsmarter.ViewModel.Profile.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class DrawerFragment extends Fragment {

    private ImageView profileImageView;
    private TextView userNameTextView;
    private ProfileViewModel profileViewModel;

    private ImageButton btnProfile;
    private ImageButton btnLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);
        profileImageView = view.findViewById(R.id.profile_image);
        userNameTextView = view.findViewById(R.id.user_name);
        btnProfile = view.findViewById(R.id.btnProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnProfile.setOnClickListener(v -> navigateToProfile());
        btnLogout.setOnClickListener(v -> showLogoutDialog());

        return view;
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            // Navigate to LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            // Ensure flags are set to clear the back stack
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // Ensure the current activity is finished
            getActivity().finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void navigateToProfile() {
        ((MainActivity) getActivity()).navigateToProfileFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
        observeProfile();

    }

    private void observeProfile()
    {
        // Observe profile data changes
        profileViewModel.getProfileData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                userNameTextView.setText(profile.name);
                if (profile.imageUri != null && !profile.imageUri.isEmpty()) {
                    Picasso.get().load(profile.imageUri).into(profileImageView);
                } else {
                    // Load a default image or leave blank
                    profileImageView.setImageResource(R.drawable.round_person_24);
                }
            }
        });
    }
}
