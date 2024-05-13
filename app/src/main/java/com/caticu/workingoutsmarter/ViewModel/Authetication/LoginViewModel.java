package com.caticu.workingoutsmarter.ViewModel.Authetication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.Authentication.AuthenticationModel;

public class LoginViewModel extends ViewModel
{
    private AuthenticationModel authenticationModel;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> signUpClicked = new MutableLiveData<>();
    private MutableLiveData<Boolean> forgotPasswordClicked = new MutableLiveData<>();
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

    public LiveData<Boolean> getSignUpClicked() {
        return signUpClicked;
    }

    public LiveData<Boolean> getForgotPasswordClicked() { return forgotPasswordClicked; }

    public void onSignUpClicked() { signUpClicked.setValue(true); }

    public void onForgotPasswordClicked() { forgotPasswordClicked.setValue(true); }

    public void onSignUpIntentHandled() { signUpClicked.setValue(false); }

    public void onForgotPasswordIntentHandled() { forgotPasswordClicked.setValue(false); }

    public void signInWithEmailPassword(String email, String password)
    {
        loading.setValue(true);
        authenticationModel.signInWithEmailPassword(email, password).addOnCompleteListener(task ->
        {
                    if (task.isSuccessful())
                    {
                        loginSuccess.setValue(true);
                    } else
                    {
                        loginError.setValue(true);
                    }
                    loading.setValue(false);
        });
    }
}
