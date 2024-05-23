package com.caticu.workingoutsmarter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.View.Fragments.HomeFragment;
import com.caticu.workingoutsmarter.View.Fragments.LogoutFragment;
import com.caticu.workingoutsmarter.View.Fragments.ProfileFragment;
import com.caticu.workingoutsmarter.View.Fragments.WorkoutFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navigationBarView = findViewById(R.id.navigation_bar);
        navigationBarView.setOnItemSelectedListener(item ->
        {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_logOut)
            {
                selectedFragment = new LogoutFragment();
            }
            else if (itemId == R.id.nav_home)
            {
                selectedFragment = new HomeFragment();
            }
            else if (itemId == R.id.nav_workout)
            {
                selectedFragment = new WorkoutFragment();
            }
            else if (itemId == R.id.nav_profile)
            {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }

            return true;
        });

        // Load the default fragment
        if (savedInstanceState == null) {
            loadFragment(new WorkoutFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
