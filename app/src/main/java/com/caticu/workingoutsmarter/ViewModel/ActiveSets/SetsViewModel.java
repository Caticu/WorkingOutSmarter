package com.caticu.workingoutsmarter.ViewModel.ActiveSets;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.ActiveWorkout.Set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetsViewModel extends ViewModel {
    private final Map<String, MutableLiveData<List<Set>>> setsMap = new HashMap<>();

    public LiveData<List<Set>> getSets(String workoutId) {
        if (!setsMap.containsKey(workoutId)) {
            setsMap.put(workoutId, new MutableLiveData<>(new ArrayList<>()));
        }
        return setsMap.get(workoutId);
    }

    public void addSet(String workoutId, Set set) {
        MutableLiveData<List<Set>> setsLiveData = setsMap.get(workoutId);
        if (setsLiveData == null) {
            setsLiveData = new MutableLiveData<>(new ArrayList<>());
            setsMap.put(workoutId, setsLiveData);
        }
        List<Set> sets = setsLiveData.getValue();
        if (sets == null) {
            sets = new ArrayList<>();
        }
        sets.add(set);
        setsLiveData.setValue(sets);
    }

    public void setSets(String workoutId, List<Set> sets) {
        if (!setsMap.containsKey(workoutId)) {
            setsMap.put(workoutId, new MutableLiveData<>(sets));
        } else {
            setsMap.get(workoutId).setValue(sets);
        }
    }
}
