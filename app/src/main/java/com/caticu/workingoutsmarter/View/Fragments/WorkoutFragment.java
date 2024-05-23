package com.caticu.workingoutsmarter.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.API.WorkoutFromAPI;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.View.Workout.WorkoutAdapter;
import com.caticu.workingoutsmarter.API.ExerciseAPIViewModel;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {

    private static final String KEY_RECYCLER_STATE = "recycler_state";
    private static final String KEY_SEARCH_QUERY = "search_query";
    private static final String KEY_WORKOUT_LIST = "workout_list";

    private ExerciseAPIViewModel exerciseViewModel;
    private WorkoutAdapter adapter;
    private EditText searchExerciseTextBox;
    private RecyclerView recyclerView;
    private Button searchButton;

    private ArrayList<WorkoutFromAPI> workoutList = new ArrayList<>();
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchExerciseTextBox = view.findViewById(R.id.searchExerciseTextBox);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchButton = view.findViewById(R.id.searchButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WorkoutAdapter(workoutList, this::showWorkoutDetailFragment);
        recyclerView.setAdapter(adapter);

        // Initialize the ViewModel
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseAPIViewModel.class);

        searchExerciseTextBox.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        searchExerciseTextBox.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                searchExerciseTextBox.setText("");
            }
        });

        searchButton.setOnClickListener(v -> performSearch());

        // Restore state if available
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY, "");
            workoutList = (ArrayList<WorkoutFromAPI>) savedInstanceState.getSerializable(KEY_WORKOUT_LIST);
            searchExerciseTextBox.setText(searchQuery);
            adapter.updateWorkoutList(workoutList);
        }

        // Listen for back stack changes to show/hide the RecyclerView and other elements
        requireActivity().getSupportFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SEARCH_QUERY, searchExerciseTextBox.getText().toString().trim());
        outState.putSerializable(KEY_WORKOUT_LIST, workoutList);
    }

    private void performSearch() {
        searchQuery = searchExerciseTextBox.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            // Show a toast message indicating data fetching
            Toast.makeText(getContext(), "Fetching data", Toast.LENGTH_SHORT).show();
            // Clear the RecyclerView before starting a new search
            adapter.updateWorkoutList(new ArrayList<>());
            exerciseViewModel.searchExercises(searchQuery).observe(getViewLifecycleOwner(), exercises -> {
                if (exercises != null && !exercises.isEmpty()) {
                    workoutList = new ArrayList<>(exercises);
                    adapter.updateWorkoutList(workoutList);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data! Type-in a correct muscle!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showWorkoutDetailFragment(WorkoutFromAPI workout) {
        WorkoutDetailFragment fragment = WorkoutDetailFragment.newInstance(workout);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Hide RecyclerView and other UI elements
        recyclerView.setVisibility(View.GONE);
        searchExerciseTextBox.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
    }

    private void onBackStackChanged() {
        if (isAdded()) { // Check if the fragment is attached to its activity
            if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
                // No fragments in back stack, show RecyclerView and other UI elements
                recyclerView.setVisibility(View.VISIBLE);
                searchExerciseTextBox.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                // Restore search query and workout list
                searchExerciseTextBox.setText(searchQuery);
                adapter.updateWorkoutList(workoutList);
            }
        }
    }
}
