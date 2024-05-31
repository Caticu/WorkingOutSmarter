package com.caticu.workingoutsmarter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.caticu.workingoutsmarter.View.Activities.LoginActivity;
import com.caticu.workingoutsmarter.View.DrawerFragment;
import com.caticu.workingoutsmarter.View.Fragments.ActiveWorkouts.ActiveWorkoutsFragment;
import com.caticu.workingoutsmarter.View.Fragments.Overview.OverviewWorkoutFragment;
import com.caticu.workingoutsmarter.View.Fragments.ProfileFragment;
import com.caticu.workingoutsmarter.View.Fragments.Workout.WorkoutFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String tak_ACtiveWorkoutFragment = "active_workouts_fragment";
    private static final String tag_workoutFragment = "workout_fragment";
    private static final String tag_profileFragment = "profile_fragment";
    private static final String tag_overviewFragment = "overview_fragment";
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
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.drawer_container, new DrawerFragment())
                    .commit();
        }
        navigationBarView = findViewById(R.id.navigation_bar);
        navigationBarView.setOnItemSelectedListener(item ->
        {
            Fragment selectedFragment = null;
            String tag = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_active_workouts)
            {
                selectedFragment = new ActiveWorkoutsFragment();
                tag = tak_ACtiveWorkoutFragment;
                toolbar.setTitle("Active workouts");
            }
            else if (itemId == R.id.nav_workout)
            {
                selectedFragment = new WorkoutFragment();
                tag = tag_workoutFragment;
                toolbar.setTitle("Workouts");
            }
            else if (itemId == R.id.nav_profile)
            {
                selectedFragment = new ProfileFragment();
                tag = tag_profileFragment;
                toolbar.setTitle("Profile");
            }
            else if (itemId == R.id.overview)
            {
                selectedFragment = new OverviewWorkoutFragment();
                tag = tag_overviewFragment;
                toolbar.setTitle("Overview");
            }

            if (selectedFragment != null)
            {
                loadFragment(selectedFragment, tag);
            }
            return true;
        });

        // Load the default fragment
        if (savedInstanceState == null)
        {
            loadFragment(new ActiveWorkoutsFragment(), tak_ACtiveWorkoutFragment);
            toolbar.setTitle("Active workouts");
        }

        // back button listener
        getSupportFragmentManager().addOnBackStackChangedListener(this::handleBackButtonVisibility);
    }

    private void createDrawerFragment()
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.drawer_container, new DrawerFragment()).commit();
    }


    private void loadFragment(Fragment fragment, String tag)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (existingFragment == null)
        {
            transaction.replace(R.id.fragment_container, fragment, tag);
            transaction.addToBackStack(tag);
        }
        else
        {
            getSupportFragmentManager().popBackStack(tag, 0);
        }
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof ActiveWorkoutsFragment)
        {
            if (id == R.id.action_delete)
            {
                ((ActiveWorkoutsFragment) fragment).deleteSelectedItems();
                return true;
            }
        }
        if (id == R.id.action_logout)
        {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", (dialog, which) ->
        {
            // logout
            FirebaseAuth.getInstance().signOut();

            // go to to LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) ->
        {
            //  return
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1)
        {
            fragmentManager.popBackStack();
        } else
        {
            super.onBackPressed();
        }
    }
    // button to navigate back
    private void handleBackButtonVisibility()
    {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
        }
        else
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toolbar.setNavigationOnClickListener(null);
        }
        // Update the toolbar based of the fragment it is in right nw
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof ActiveWorkoutsFragment)
        {
            toolbar.setTitle("Active workouts");
        }
        else if (currentFragment instanceof WorkoutFragment)
        {
            toolbar.setTitle("Workouts");
        }
        else if (currentFragment instanceof ProfileFragment)
        {
            toolbar.setTitle("Profile");
        }
        else if (currentFragment instanceof OverviewWorkoutFragment)
        {
            toolbar.setTitle("Overview");
        }
    }
    public void navigateToProfileFragment()
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new ProfileFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
