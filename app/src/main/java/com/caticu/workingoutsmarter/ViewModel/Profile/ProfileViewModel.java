package com.caticu.workingoutsmarter.ViewModel.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel
{
    private MutableLiveData<String> name = new MutableLiveData<>();
    private MutableLiveData<Integer> age = new MutableLiveData<>();
    private MutableLiveData<Double> height = new MutableLiveData<>();
    private MutableLiveData<Double> weight = new MutableLiveData<>();
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> saveError = new MutableLiveData<>();

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<Integer> getAge() {
        return age;
    }

    public LiveData<Double> getHeight() {
        return height;
    }

    public LiveData<Double> getWeight() {
        return weight;
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<Boolean> getSaveError() {
        return saveError;
    }

    public void saveProfile(String name, int age, double height, double weight)
    {
        // Your logic to save the profile data to Firebase goes here
        // This method should update saveSuccess and saveError MutableLiveData
    }
    public ProfileViewModel()
    {
    }
}
