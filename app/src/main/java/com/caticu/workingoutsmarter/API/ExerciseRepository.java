package com.caticu.workingoutsmarter.API;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.caticu.workingoutsmarter.API.ExerciseAPIService;
import com.caticu.workingoutsmarter.API.RetrofitClientInstance;
import com.caticu.workingoutsmarter.API.WorkoutFromAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseRepository {
    private static ExerciseRepository instance;
    private final ExerciseAPIService apiService;

    private ExerciseRepository() {
        apiService = RetrofitClientInstance.getRetrofitInstance().create(ExerciseAPIService.class);
    }

    public static synchronized ExerciseRepository getInstance() {
        if (instance == null) {
            instance = new ExerciseRepository();
        }
        return instance;
    }

    public LiveData<List<WorkoutFromAPI>> searchExercises(String muscle) {
        MutableLiveData<List<WorkoutFromAPI>> data = new MutableLiveData<>();
        apiService.searchExercises(muscle).enqueue(new Callback<List<WorkoutFromAPI>>() {
            @Override
            public void onResponse(Call<List<WorkoutFromAPI>> call, Response<List<WorkoutFromAPI>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);

                }
            }

            @Override
            public void onFailure(Call<List<WorkoutFromAPI>> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
