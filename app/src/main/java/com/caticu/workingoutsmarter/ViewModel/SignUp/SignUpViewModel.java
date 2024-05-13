package com.caticu.workingoutsmarter.ViewModel.SignUp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.SignUp.SignUpModel;

public class SignUpViewModel extends ViewModel {
    private SignUpModel signUpModel;
    private MutableLiveData<Boolean> signUpSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> signUpError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public SignUpViewModel() {
        signUpModel = new SignUpModel();
    }

    public LiveData<Boolean> getSignUpSuccess() {
        return signUpSuccess;
    }

    public LiveData<Boolean> getSignUpError() {
        return signUpError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void createUserWithEmailAndPassword(String email, String password) {
        loading.setValue(true);
        signUpModel.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        signUpSuccess.setValue(true);
                    } else {
                        signUpError.setValue(true);
                    }
                    loading.setValue(false);
                });
    }
}
