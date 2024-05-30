package com.caticu.workingoutsmarter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.caticu.workingoutsmarter.View.Activities.LoginActivity;
import com.caticu.workingoutsmarter.View.DrawerFragment;
import com.caticu.workingoutsmarter.View.Fragments.ActiveWorkouts.ActiveWorkoutsFragment;
import com.caticu.workingoutsmarter.View.Fragments.Overview.OverviewWorkoutFragment;
import com.caticu.workingoutsmarter.View.Fragments.ProfileFragment;
import com.caticu.workingoutsmarter.View.Fragments.Workout.WorkoutFragment;
import com.caticu.workingoutsmarter.ViewModel.Profile.ProfileViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_ACTIVE_WORKOUTS_FRAGMENT = "active_workouts_fragment";
    private static final String TAG_WORKOUT_FRAGMENT = "workout_fragment";
    private static final String TAG_PROFILE_FRAGMENT = "profile_fragment";
    private static final String TAG_OVERVIEW_FRAGMENT = "overview_fragment";
    private BottomNavigationView navigationBarView;
    private Toolbar toolbar;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        createDrawerFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.drawer_container, new DrawerFragment())
                    .commit();
        }


        navigationBarView = findViewById(R.id.navigation_bar);
        navigationBarView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            String tag = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_active_workouts) {
                selectedFragment = new ActiveWorkoutsFragment();
                tag = TAG_ACTIVE_WORKOUTS_FRAGMENT;
                toolbar.setTitle("Active workouts");
            } else if (itemId == R.id.nav_workout) {
                selectedFragment = new WorkoutFragment();
                tag = TAG_WORKOUT_FRAGMENT;
                toolbar.setTitle("Workouts");
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
                tag = TAG_PROFILE_FRAGMENT;
                toolbar.setTitle("Profile");
            } else if (itemId == R.id.overview) {
                selectedFragment = new OverviewWorkoutFragment();
                tag = TAG_OVERVIEW_FRAGMENT;
                toolbar.setTitle("Overview");
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, tag);
            }

            return true;
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            loadFragment(new ActiveWorkoutsFragment(), TAG_ACTIVE_WORKOUTS_FRAGMENT);
            toolbar.setTitle("Active workouts");
        }

        // Handle the back button on the toolbar
        getSupportFragmentManager().addOnBackStackChangedListener(this::handleBackButtonVisibility);
    }

    private void createDrawerFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.drawer_container, new DrawerFragment())
                .commit();
    }


    private void loadFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (existingFragment == null) {
            transaction.replace(R.id.fragment_container, fragment, tag);
            transaction.addToBackStack(tag);
        } else {
            getSupportFragmentManager().popBackStack(tag, 0);
        }

        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ActiveWorkoutsFragment) {
            if (id == R.id.action_delete) {
                ((ActiveWorkoutsFragment) fragment).deleteSelectedItems();
                return true;
            }
        }
        if (id == R.id.action_logout) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Perform logout action using Firebase Authentication
            FirebaseAuth.getInstance().signOut();

            // Navigate to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Close the dialog
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            super.onBackPressed();  // Call super to handle the default back press behavior
        }
    }

    private void handleBackButtonVisibility() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolbar.setNavigationOnClickListener(null);
        }

        // Update the toolbar title based on the current fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ActiveWorkoutsFragment) {
            toolbar.setTitle("Active workouts");
        } else if (currentFragment instanceof WorkoutFragment) {
            toolbar.setTitle("Workouts");
        } else if (currentFragment instanceof ProfileFragment) {
            toolbar.setTitle("Profile");
        } else if (currentFragment instanceof OverviewWorkoutFragment) {
            toolbar.setTitle("Overview");
        }
    }


    public void navigateToProfileFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ProfileFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
