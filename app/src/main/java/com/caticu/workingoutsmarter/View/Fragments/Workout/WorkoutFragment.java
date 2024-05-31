package com.caticu.workingoutsmarter.View.Fragments.Workout;

import static com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel.convertToActiveWorkoutModel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.API.WorkoutFromAPI;
import com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.Repository.WorkoutRepository;
import com.caticu.workingoutsmarter.View.Adapter.WorkoutAdapter;
import com.caticu.workingoutsmarter.API.ExerciseAPIViewModel;
import com.caticu.workingoutsmarter.View.Fragments.Workout.WorkoutDetailFragment;
import com.caticu.workingoutsmarter.ViewModel.ActiveWorkouts.ActiveWorkoutViewModel;
import com.caticu.workingoutsmarter.ViewModel.ActiveWorkouts.ActiveWorkoutViewModelFactory;

import java.util.ArrayList;

public class WorkoutFragment extends Fragment {

    private ExerciseAPIViewModel exerciseViewModel;
    private WorkoutAdapter adapter;
    private EditText searchExerciseTextBox;
    private Spinner muscleSpinner;
    private RecyclerView recyclerView;
    private Button searchButton;
    private TextView emptyListTextView;
    private ProgressBar progressBar;
    private ArrayList<WorkoutFromAPI> workoutList = new ArrayList<>();
    private String searchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchExerciseTextBox = view.findViewById(R.id.searchExerciseTextBox);
        muscleSpinner = view.findViewById(R.id.muscleSpinner);
        recyclerView = view.findViewById(R.id.recyclerView);
        searchButton = view.findViewById(R.id.searchButton);
        emptyListTextView = view.findViewById(R.id.emptyListTextView);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WorkoutAdapter(workoutList, new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WorkoutFromAPI workout) {
                showWorkoutDetailFragment(workout);
            }

            @Override
            public void onAddButtonClick(WorkoutFromAPI workout) {
                addWorkoutToActiveWorkouts(workout);
            }
        });

        recyclerView.setAdapter(adapter);

        exerciseViewModel = new ViewModelProvider(requireActivity()).get(ExerciseAPIViewModel.class);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.muscle_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muscleSpinner.setAdapter(spinnerAdapter);

        muscleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedMuscle = parent.getItemAtPosition(position).toString();
                searchExerciseTextBox.setText(selectedMuscle);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        searchButton.setOnClickListener(v -> performSearch());

        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString("search_query", "");
            workoutList = (ArrayList<WorkoutFromAPI>) savedInstanceState.getSerializable("workout_list");
            searchExerciseTextBox.setText(searchQuery);
            adapter.updateWorkoutList(workoutList);
            updateEmptyListMessage(workoutList.isEmpty());
        } else {
            exerciseViewModel.getExercises().observe(getViewLifecycleOwner(), exercises -> {
                if (exercises != null && !exercises.isEmpty()) {
                    workoutList = new ArrayList<>(exercises);
                    adapter.updateWorkoutList(workoutList);
                    updateEmptyListMessage(workoutList.isEmpty());
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "No exercises found.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        requireActivity().getSupportFragmentManager().addOnBackStackChangedListener(this::onBackStackChanged);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search_query", searchExerciseTextBox.getText().toString().trim());
        outState.putSerializable("workout_list", workoutList);
    }

    private void performSearch() {
        searchQuery = searchExerciseTextBox.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            Toast.makeText(getContext(), "Fetching data", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.VISIBLE);
            adapter.updateWorkoutList(new ArrayList<>());
            updateEmptyListMessage(true);
            exerciseViewModel.searchExercises(searchQuery).observe(getViewLifecycleOwner(), exercises -> {
                if (exercises != null && !exercises.isEmpty()) {
                    workoutList = new ArrayList<>(exercises);
                    exerciseViewModel.setExercises(workoutList);
                    adapter.updateWorkoutList(workoutList);
                    updateEmptyListMessage(false);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data! Type-in a correct muscle!", Toast.LENGTH_SHORT).show();
                    updateEmptyListMessage(true);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void updateEmptyListMessage(boolean show) {
        emptyListTextView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showWorkoutDetailFragment(WorkoutFromAPI workout) {
        WorkoutDetailFragment fragment = WorkoutDetailFragment.newInstance(workout);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addWorkoutToActiveWorkouts(WorkoutFromAPI workoutFromAPI) {
        ActiveWorkoutModel activeWorkoutModel = convertToActiveWorkoutModel(workoutFromAPI);
        ActiveWorkoutViewModel viewModel = new ViewModelProvider(requireActivity(), new ActiveWorkoutViewModelFactory(WorkoutRepository.getInstance())).get(ActiveWorkoutViewModel.class);
        viewModel.addWorkout(activeWorkoutModel);
        Toast.makeText(getContext(), "Exercise added to active workouts!", Toast.LENGTH_SHORT).show();
    }

    private void onBackStackChanged() {
        if (isAdded()) {
            if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
                recyclerView.setVisibility(View.VISIBLE);
                searchExerciseTextBox.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                searchExerciseTextBox.setText(searchQuery);
                adapter.updateWorkoutList(workoutList);
                updateEmptyListMessage(workoutList.isEmpty());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded()) {
            requireActivity().setTitle("Search for muscle");
        }
        updateEmptyListMessage(workoutList.isEmpty());
    }
}
