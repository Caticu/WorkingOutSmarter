package com.caticu.workingoutsmarter.View.Fragments.Overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caticu.workingoutsmarter.Model.WorkoutToDb.Workout;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.View.Adapter.WorkoutNameAdapter;
import com.caticu.workingoutsmarter.ViewModel.Overview.OverviewWorkoutViewModel;
import com.caticu.workingoutsmarter.ViewModel.Overview.OverviewWorkoutViewModelFactory;
import com.caticu.workingoutsmarter.Repository.WorkoutRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class OverviewWorkoutFragment extends Fragment {

    private RecyclerView recyclerViewOverview;
    private WorkoutNameAdapter workoutNameAdapter;
    private OverviewWorkoutViewModel overviewWorkoutViewModel;
    private CalendarView calendarView;
    private TextView textSelectDateMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewOverview = view.findViewById(R.id.recyclerViewOverview);
        recyclerViewOverview.setLayoutManager(new LinearLayoutManager(getContext()));

        workoutNameAdapter = new WorkoutNameAdapter();
        recyclerViewOverview.setAdapter(workoutNameAdapter);

        calendarView = view.findViewById(R.id.calendarView);
        textSelectDateMessage = view.findViewById(R.id.textSelectDateMessage);

        overviewWorkoutViewModel = new ViewModelProvider(
                requireActivity(),
                new OverviewWorkoutViewModelFactory(WorkoutRepository.getInstance())
        ).get(OverviewWorkoutViewModel.class);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Observe the selected date
        overviewWorkoutViewModel.getSelectedDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String date) {
                if (date != null) {
                    fetchWorkoutsForDate(date, userId);
                }
            }
        });

        // Set listener for the calendar view
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                overviewWorkoutViewModel.setSelectedDate(selectedDate);
            }
        });

        // Handle item clicks
        workoutNameAdapter.setOnItemClickListener(new WorkoutNameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Workout workout) {
                // Navigate to OverviewWorkoutDetailFragment
                OverviewWorkoutDetailFragment fragment = OverviewWorkoutDetailFragment.newInstance(workout);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void fetchWorkoutsForDate(String date, String userId) {
        overviewWorkoutViewModel.getWorkoutsForDate(date, userId).observe(getViewLifecycleOwner(), new Observer<List<Workout>>() {
            @Override
            public void onChanged(List<Workout> workouts) {
                if (workouts != null && !workouts.isEmpty()) {
                    // Update RecyclerView with workout names
                    workoutNameAdapter.setWorkouts(workouts);
                    textSelectDateMessage.setVisibility(View.GONE);
                } else {
                    workoutNameAdapter.setWorkouts(new ArrayList<>());
                    textSelectDateMessage.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "No workouts found for this date.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().setTitle("Overview");
        }
    }
}
