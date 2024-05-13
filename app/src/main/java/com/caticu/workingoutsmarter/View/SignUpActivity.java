package com.caticu.workingoutsmarter.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.ViewModel.SignUp.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity
{
    private EditText name;
    private EditText email;
    private EditText password;
    private Button createAccountButton;
    private ProgressBar progressBar;
    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.NameBox);
        email = findViewById(R.id.EmailBox);
        password = findViewById(R.id.PasswordBox);
        createAccountButton = findViewById(R.id.CreateAccountButton);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        createAccountButton.setOnClickListener(view -> {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            signUpViewModel.createUserWithEmailAndPassword(userEmail, userPassword);
        });

        signUpViewModel.getSignUpSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(SignUpActivity.this, "The account has been created!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        signUpViewModel.getSignUpError().observe(this, error -> {
            if (error) {
                Toast.makeText(SignUpActivity.this, "There was a problem creating your account!", Toast.LENGTH_LONG).show();
            }
        });

        signUpViewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
            createAccountButton.setEnabled(!loading);
        });
    }
}
