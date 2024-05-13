package com.caticu.workingoutsmarter.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.AuthenticationModel;

public class LoginViewModel extends ViewModel
{
    private AuthenticationModel authenticationModel;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public LoginViewModel() {
        authenticationModel = new AuthenticationModel();
    }

    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<Boolean> getLoginError() {
        return loginError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void signInWithEmailPassword(String email, String password) {
        loading.setValue(true);
        authenticationModel.signInWithEmailPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginSuccess.setValue(true);
                    } else {
                        loginError.setValue(true);
                    }
                    loading.setValue(false);
                });
    }
}
