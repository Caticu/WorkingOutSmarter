package com.caticu.workingoutsmarter.ViewModel.Profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.Profile.Profile;
import com.caticu.workingoutsmarter.Model.Profile.ProfileModel;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> saveError = new MutableLiveData<>();
    private MutableLiveData<Profile> profileData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private ProfileModel profileModel = new ProfileModel();

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<Boolean> getSaveError() {
        return saveError;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }


    public LiveData<Profile> getProfileData() {
        return profileData;
    }

    // Existing method without callback
    public void fetchProfile(String userId) {
        profileModel.getProfile(userId, new ProfileModel.OnProfileDataCallback() {
            @Override
            public void onProfileDataLoaded(Profile profile) {
                profileData.postValue(profile);
                isLoading.postValue(false);
            }

            @Override
            public void onProfileDataNotAvailable(String errorMessage) {
                saveError.postValue(true);
            }
        });
    }

    // Overloaded method with a callback
    public void fetchProfile(String userId, final DataReadyCallback callback) {
        profileModel.getProfile(userId, new ProfileModel.OnProfileDataCallback() {
            @Override
            public void onProfileDataLoaded(Profile profile) {
                profileData.postValue(profile);
                if (callback != null) {
                    callback.onDataReady(profile);
                }
            }

            @Override
            public void onProfileDataNotAvailable(String errorMessage) {
                saveError.postValue(true);
                if (callback != null) {
                    callback.onDataNotAvailable();
                }
            }
        });
    }

    public interface DataReadyCallback {
        void onDataReady(Profile profile);
        void onDataNotAvailable();
    }

    public void saveProfile(String userId, String name, int age, double height, double weight, String imageUri) {
        profileModel.saveProfile(userId, name, age, height, weight, imageUri, new ProfileModel.OnProfileSaveCallback() {
            @Override
            public void onProfileSaveSuccess() {
                saveSuccess.postValue(true);
                fetchProfile(userId); // Refresh profile data after save
            }

            @Override
            public void onProfileSaveFailure(Exception e) {
                saveError.postValue(true);
            }
        });
    }
}
