package com.caticu.workingoutsmarter;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.ActiveWorkoutModel;
import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;
import com.caticu.workingoutsmarter.Repository.IWorkoutRepository;
import com.caticu.workingoutsmarter.ViewModel.ActiveWorkouts.ActiveWorkoutViewModel;

public class ActiveWorkoutViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ActiveWorkoutViewModel activeWorkoutViewModel;

    @Mock
    private IWorkoutRepository workoutRepository;

    @Mock
    private Observer<List<ActiveWorkoutModel>> activeWorkoutsObserver;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activeWorkoutViewModel = new ActiveWorkoutViewModel(workoutRepository);
        activeWorkoutViewModel.getActiveWorkouts().observeForever(activeWorkoutsObserver);
    }

    @Test
    public void testAddWorkout() {
        // Create a new workout with sets
        List<Set> sets = new ArrayList<>();
        sets.add(new Set(1, 50,0));
        sets.add(new Set(2, 60,0));

        ActiveWorkoutModel newWorkout = new ActiveWorkoutModel("push ups", "nothing", "do push ups", sets);

        // Reset the observer to clear any previous invocations
        // had problem because it was observing too manu times
        reset(activeWorkoutsObserver);

        // Add the workout to the ViewModel
        activeWorkoutViewModel.addWorkout(newWorkout);

        // Capture the current list of workouts
        ArgumentCaptor<List<ActiveWorkoutModel>> captor = ArgumentCaptor.forClass(List.class);
        verify(activeWorkoutsObserver, times(1)).onChanged(captor.capture());

        List<ActiveWorkoutModel> currentWorkouts = captor.getValue();

        // Check if the new workout is in the list
        assertTrue(currentWorkouts.contains(newWorkout));
    }
}