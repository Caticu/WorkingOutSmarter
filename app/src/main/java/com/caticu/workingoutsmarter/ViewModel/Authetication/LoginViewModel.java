package com.caticu.workingoutsmarter.ViewModel.Authetication;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.caticu.workingoutsmarter.Model.Authentication.AuthenticationModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginViewModel extends AndroidViewModel
{
    private AuthenticationModel authenticationModel;
    private MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<Boolean> signUpClicked = new MutableLiveData<>();
    private MutableLiveData<Boolean> forgotPasswordClicked = new MutableLiveData<>();
    private MutableLiveData<Intent> googleSignInIntent = new MutableLiveData<>();
    private MutableLiveData<Boolean> googleSignInSuccess = new MutableLiveData<>();
    private GoogleSignInClient googleSignInClient;

    public LoginViewModel(@NonNull Application application)
    {
        super(application);
        authenticationModel = new AuthenticationModel();
        initGoogleSignIn();
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

    public LiveData<Intent> getGoogleSignInIntent() {return googleSignInIntent; }

    public LiveData<Boolean> getGoogleSignInSuccess() { return googleSignInSuccess; }

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

    public void signInWithGoogle() {
        googleSignInIntent.setValue(googleSignInClient.getSignInIntent());
    }

    private void initGoogleSignIn()
    {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("264762762980-cus4762gbsvqtjl5ua9l1ga6c5s1l4pf.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(getApplication(), googleSignInOptions);
    }

    public void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task)
    {
        try
        {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                authenticationModel.signInWithGoogle(authCredential).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        googleSignInSuccess.setValue(true);
                    } else {
                        loginError.setValue(true);
                    }
                });
            }
        }
        catch (ApiException e)
        {
            e.printStackTrace();
            loginError.setValue(true);
        }
    }
}
