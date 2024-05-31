package com.caticu.workingoutsmarter.View.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.caticu.workingoutsmarter.MainActivity;
import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.ViewModel.Authetication.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity
{
    private EditText mail;
    private EditText password;
    private Button login;
    private TextView signUp;
    private SignInButton signInGoogle;
    private TextView forgotPassword;
    private ProgressBar progressBarSignIn;
    private LoginViewModel loginViewModel;
    GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mail = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.LogInButton);
        progressBarSignIn = findViewById(R.id.progressBarSignIn);
        progressBarSignIn.setVisibility(View.INVISIBLE);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        signUp = findViewById(R.id.NoAccountTextView);
        forgotPassword = findViewById(R.id.ForgotPasswordTextView);
        signInGoogle = findViewById(R.id.signInButtonGoogle);

        login.setOnClickListener(view -> signIn());
        signUp.setOnClickListener(view -> loginViewModel.onSignUpClicked());
        forgotPassword.setOnClickListener(view -> loginViewModel.onForgotPasswordClicked());
        registerActivityForGoogle();
        signInGoogle.setOnClickListener(view -> loginViewModel.signInWithGoogle());

        checkIfUserIsLoggedIn();
        mail.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
            {
                mail.setText("");
            }
        });
        password.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
            {
                password.setText("");
            }
        });
        // Observe login success
        loginViewModel.getLoginSuccess().observe(this, success ->
        {
            if (success)
            {
                // Handle successful login
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Observe login error
        loginViewModel.getLoginError().observe(this, error ->
        {
            if (error)
            {
                // Handle failed login
                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
            }
        });

        // Observe loading state
        loginViewModel.getLoading().observe(this, loading ->
        {
            progressBarSignIn.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
            login.setEnabled(!loading);
        });

        loginViewModel.getSignUpClicked().observe(this, signUpClicked ->
        {
            if (signUpClicked)
            {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                loginViewModel.onSignUpIntentHandled();
            }
        });

        loginViewModel.getForgotPasswordClicked().observe(this, forgotPasswordClicked ->
        {
            if (forgotPasswordClicked)
            {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                loginViewModel.onForgotPasswordIntentHandled();
            }
        });

        loginViewModel.getGoogleSignInIntent().observe(this, intent ->
        {
            if (intent != null)
            {
                activityResultLauncher.launch(intent);
            }
        });

        loginViewModel.getGoogleSignInSuccess().observe(this, success ->
        {
            if (success)
            {
                // Handle successful Google sign-in
                Toast.makeText(this, "Google Sign-In successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkIfUserIsLoggedIn()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, go to WorkoutActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn()
    {
        String userEmail = mail.getText().toString();
        String userPassword = password.getText().toString();

        loginViewModel.signInWithEmailPassword(userEmail, userPassword);
    }

    private void registerActivityForGoogle()
    {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->
        {
            if (result.getResultCode() == RESULT_OK)
            {
                Intent data = result.getData();
                if (data != null)
                {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    loginViewModel.firebaseSignInWithGoogle(task);
                }
            }
        });
    }
}
