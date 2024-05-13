package com.caticu.workingoutsmarter.ViewModel.ResetPassword;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.Task;

import com.caticu.workingoutsmarter.Model.ResetPassword.ResetPasswordModel;

public class ResetPasswordViewModel extends ViewModel
{
    private ResetPasswordModel resetPasswordModel;
    private MutableLiveData<Boolean> resetSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> resetError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    public ResetPasswordViewModel() {
        resetPasswordModel = new ResetPasswordModel();
    }

    public LiveData<Boolean> getResetSuccess() {
        return resetSuccess;
    }

    public LiveData<Boolean> getResetError() {
        return resetError;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public void resetPassword(String email) {
        loading.setValue(true);
        resetPasswordModel.resetPassword(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        resetSuccess.setValue(true);
                    } else {
                        resetError.setValue(true);
                    }
                    loading.setValue(false);
                });
    }
}
