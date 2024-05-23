package com.caticu.workingoutsmarter.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ExerciseAPIService
{
    @GET("exercises")
    Call<List<WorkoutFromAPI>> searchExercises(@Query("muscle") String muscle);
}
