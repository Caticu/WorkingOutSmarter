package com.caticu.workingoutsmarter.View.Fragments.ActiveWorkouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel;
import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.View.Adapter.SetsAdapter;
import com.caticu.workingoutsmarter.ViewModel.ActiveSets.SetsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ActiveWorkoutsDetailFragment extends Fragment {

    private static final String ARG_WORKOUT = "workout";

    private ActiveWorkoutModel workout;
    private SetsAdapter setsAdapter;
    private SetsViewModel setsViewModel;

    public static ActiveWorkoutsDetailFragment newInstance(ActiveWorkoutModel workout) {
        ActiveWorkoutsDetailFragment fragment = new ActiveWorkoutsDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT, workout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workout = (ActiveWorkoutModel) getArguments().getSerializable(ARG_WORKOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_active_workouts_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setsViewModel = new ViewModelProvider(requireActivity()).get(SetsViewModel.class);

        TextView nameTextView = view.findViewById(R.id.NameTextView);
        Button addButton = view.findViewById(R.id.addButton);
        RecyclerView recyclerViewSets = view.findViewById(R.id.recyclerViewSets);

        if (workout != null) {
            nameTextView.setText(workout.getName());
        }

        setsAdapter = new SetsAdapter();
        recyclerViewSets.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSets.setAdapter(setsAdapter);

        setsViewModel.getSets(workout.getId()).observe(getViewLifecycleOwner(), new Observer<List<Set>>() {
            @Override
            public void onChanged(List<Set> sets) {
                setsAdapter.setSets(sets);
            }
        });

        addButton.setOnClickListener(v -> {
            int setNumber = setsAdapter.getItemCount() + 1;
            Set newSet = new Set(setNumber, 0, 0);
            setsViewModel.addSet(workout.getId(), newSet);
        });

        // Restore the saved state if available
        if (savedInstanceState != null) {
            List<Set> savedSets = (List<Set>) savedInstanceState.getSerializable("sets_" + workout.getId());
            if (savedSets != null) {
                setsViewModel.setSets(workout.getId(), savedSets);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("sets_" + workout.getId(), (ArrayList<Set>) setsViewModel.getSets(workout.getId()).getValue());
    }
}