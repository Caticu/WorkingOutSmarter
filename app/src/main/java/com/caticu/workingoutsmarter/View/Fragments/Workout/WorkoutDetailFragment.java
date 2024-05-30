package com.caticu.workingoutsmarter.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.caticu.workingoutsmarter.API.WorkoutFromAPI;
import com.caticu.workingoutsmarter.R;

public class WorkoutDetailFragment extends Fragment {

    private static final String ARG_WORKOUT = "workout";

    private WorkoutFromAPI workout;

    public static WorkoutDetailFragment newInstance(WorkoutFromAPI workout) {
        WorkoutDetailFragment fragment = new WorkoutDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT, workout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workout = (WorkoutFromAPI) getArguments().getSerializable(ARG_WORKOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView nameTextView = view.findViewById(R.id.NameTextView);
        TextView equipmentTextView = view.findViewById(R.id.EquipmentTextView);
        TextView instructionsTextView = view.findViewById(R.id.InstructionsTextView);

        if (workout != null) {
            nameTextView.setText(workout.getName());
            equipmentTextView.setText(workout.getEquipment());
            instructionsTextView.setText(workout.getInstructions());
        }
    }
}
