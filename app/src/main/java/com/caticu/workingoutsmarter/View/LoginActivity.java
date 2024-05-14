package com.caticu.workingoutsmarter.View;

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

import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.ViewModel.Authetication.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;


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
    protected void onCreate(Bundle savedInstanceState) {
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

        // Observe login success
        loginViewModel.getLoginSuccess().observe(this, success ->
        {
            if (success)
            {
                // Handle successful login
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
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
                Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
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
                Intent intent = new Intent(LoginActivity.this, ResetPassword.class);
                startActivity(intent);
                finish();
            }
        });
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
     /*
    private void signInWithEmailPassword(String userEmail, String userPassword)
    {
        progressBarSignIn.setVisibility(View.VISIBLE);
        login.setClickable(false);

        firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Intent intent = new Intent(Login_Page.this, ForgotPasswordActivity.class);
                    startActivity(intent);
                    finish();
                    progressBarSignIn.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login_Page.this, "Login successful", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Login_Page.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
 */

}
