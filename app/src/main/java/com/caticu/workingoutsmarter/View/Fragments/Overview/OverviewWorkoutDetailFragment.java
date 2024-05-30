package com.caticu.workingoutsmarter.View.Fragments.Overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.caticu.workingoutsmarter.Model.WorkoutToDb.Workout;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.View.Adapter.SetsOverviewAdapter;
import com.caticu.workingoutsmarter.ViewModel.ActiveSets.SetsViewModel;

import java.util.List;

public class OverviewWorkoutDetailFragment extends Fragment {

    private static final String ARG_WORKOUT = "workout";

    private Workout workout;
    private SetsOverviewAdapter setsAdapter;
    private SetsViewModel setsViewModel;

    public static OverviewWorkoutDetailFragment newInstance(Workout workout) {
        OverviewWorkoutDetailFragment fragment = new OverviewWorkoutDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT, workout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workout = (Workout) getArguments().getSerializable(ARG_WORKOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview_workout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setsViewModel = new ViewModelProvider(requireActivity()).get(SetsViewModel.class);

        RecyclerView recyclerViewOverviewSets = view.findViewById(R.id.recyclerViewOverviewSets);
        setsAdapter = new SetsOverviewAdapter();
        recyclerViewOverviewSets.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOverviewSets.setAdapter(setsAdapter);

        if (workout != null) {
            setsAdapter.setSets(workout.getSets());
        }
    }
}
