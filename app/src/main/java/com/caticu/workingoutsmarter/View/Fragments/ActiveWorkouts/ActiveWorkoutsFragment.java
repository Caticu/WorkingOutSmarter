package com.caticu.workingoutsmarter.View.Fragments.ActiveWorkouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel;
import com.caticu.workingoutsmarter.Model.WorkoutToDb.Workout;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.Repository.WorkoutRepository;
import com.caticu.workingoutsmarter.View.Adapter.ActiveWorkoutsAdapter;
import com.caticu.workingoutsmarter.ViewModel.ActiveSets.SetsViewModel;
import com.caticu.workingoutsmarter.ViewModel.ActiveWorkouts.ActiveWorkoutViewModel;
import com.caticu.workingoutsmarter.ViewModel.ActiveWorkouts.ActiveWorkoutViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ActiveWorkoutsFragment extends Fragment implements ActiveWorkoutsAdapter.OnItemLongClickListener, ActiveWorkoutsAdapter.OnItemClickListener, DatePickerFragment.OnDateSelectedListener {

    private RecyclerView recyclerView;
    private ActiveWorkoutsAdapter adapter;
    private List<ActiveWorkoutModel> selectedItems = new ArrayList<>();
    private ActiveWorkoutViewModel activeWorkoutViewModel;
    private Button buttonFinishWorkout;
    private String selectedDate;
    private SetsViewModel setsViewModel;
    private TextView emptyListTextView;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // This is important to notify the fragment it has menu items
        return inflater.inflate(R.layout.fragment_active_workouts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewActiveWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ActiveWorkoutsAdapter(new ArrayList<>(), this, this);
        recyclerView.setAdapter(adapter);

        emptyListTextView = view.findViewById(R.id.emptyListTextView);
        buttonFinishWorkout = view.findViewById(R.id.buttonFinishWorkout);

        activeWorkoutViewModel = new ViewModelProvider(
                requireActivity(),
                new ActiveWorkoutViewModelFactory(WorkoutRepository.getInstance())
        ).get(ActiveWorkoutViewModel.class);

        setsViewModel = new ViewModelProvider(requireActivity()).get(SetsViewModel.class);

        activeWorkoutViewModel.getActiveWorkouts().observe(getViewLifecycleOwner(), new Observer<List<ActiveWorkoutModel>>() {
            @Override
            public void onChanged(List<ActiveWorkoutModel> activeWorkouts) {
                adapter.setActiveWorkouts(activeWorkouts);
                updateEmptyListMessage(activeWorkouts.isEmpty());
            }
        });

        buttonFinishWorkout.setOnClickListener(v -> openDatePicker());
    }

    @Override
    public void onItemLongClick(View view, ActiveWorkoutModel workout) {
        toggleSelection(workout);
    }

    @Override
    public void onItemClick(View view, ActiveWorkoutModel workout) {
        // Navigate to ActiveWorkoutsDetailFragment
        ActiveWorkoutsDetailFragment fragment = ActiveWorkoutsDetailFragment.newInstance(workout);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDateSelected(String date) {
        selectedDate = date;
        finishWorkout();
    }

    private void openDatePicker() {
        DatePickerFragment datePickerFragment = new DatePickerFragment(this);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, datePickerFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void toggleSelection(ActiveWorkoutModel workout) {
        if (selectedItems.contains(workout)) {
            selectedItems.remove(workout);
        } else {
            selectedItems.add(workout);
        }
        adapter.setSelectedItems(selectedItems);
        updateToolbar();
    }

    public void deleteSelectedItems() {
        for (ActiveWorkoutModel workout : selectedItems) {
            activeWorkoutViewModel.removeWorkout(workout);
        }
        selectedItems.clear();
        updateToolbar();
    }

    private void finishWorkout() {
        List<ActiveWorkoutModel> activeWorkouts = activeWorkoutViewModel.getActiveWorkouts().getValue();
        if (activeWorkouts != null && !activeWorkouts.isEmpty()) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            List<Workout> workouts = new ArrayList<>();
            for (ActiveWorkoutModel activeWorkout : activeWorkouts) {
                List<com.caticu.workingoutsmarter.Model.ActiveWorkout.Set> sets = setsViewModel.getSets(activeWorkout.getId()).getValue();
                if (sets == null) {
                    sets = new ArrayList<>();
                }
                workouts.add(new Workout(activeWorkout.getName(), sets, selectedDate));
            }
            activeWorkoutViewModel.saveWorkouts(userId, workouts);
            Toast.makeText(getContext(), "Workout saved successfully!", Toast.LENGTH_SHORT).show();
            activeWorkoutViewModel.clearActiveWorkouts();
            adapter.setActiveWorkouts(new ArrayList<>());
            updateEmptyListMessage(true);
        } else {
            Toast.makeText(getContext(), "No workouts to save!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyListMessage(boolean isEmpty) {
        emptyListTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        buttonFinishWorkout.setEnabled(!isEmpty);

        if (isEmpty) {
            showEmptyListDialog();
        }
    }

    private void showEmptyListDialog() {
        if (getActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("No exercises are selected. Please navigate to the Workouts page and add one onto the list!")
                    .setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void updateToolbar() {
        ((AppCompatActivity) requireActivity()).invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        deleteItem.setVisible(!selectedItems.isEmpty());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().setTitle("Active workouts");
        }
        List<ActiveWorkoutModel> activeWorkouts = activeWorkoutViewModel.getActiveWorkouts().getValue();
        if (activeWorkouts != null) {
            updateEmptyListMessage(activeWorkouts.isEmpty());
        } else {
            updateEmptyListMessage(true);
        }
    }
}
