package com.caticu.workingoutsmarter;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.credentials.CredentialManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Page extends AppCompatActivity
{
    // Define the things used in xml file
    EditText mail;
    EditText password;
    Button login;
    SignInButton loginGoogle;
    TextView signUp;
    TextView forgotPassword;
    ProgressBar progressBarSignIn;

    FirebaseAuth firebaseAuth;
    ActivityResultLauncher<Intent> activityResultLauncher;



    protected void onCreate(Bundle savedInstanceState)
    {
        firebaseAuth = FirebaseAuth.getInstance();




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



        // Bind the elements from xml
        mail = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.LogInButton);
        loginGoogle = findViewById(R.id.signInButtonGoogle);
        signUp = findViewById(R.id.NoAccountTextView);
        forgotPassword = findViewById(R.id.ForgotPasswordTextView);
        progressBarSignIn = findViewById(R.id.progressBarSignIn);

        progressBarSignIn.setVisibility(View.INVISIBLE);


        // Listener for sign in button
        // It is using firebase to log-in
        login.setOnClickListener(view ->
        {
            String userEmail = mail.getText().toString();
            String userPassword = password.getText().toString();
            signInWithEmailPassword(userEmail, userPassword);
        });


        // Listener to sign in using google account
        loginGoogle.setOnClickListener(view ->
        {
            signInWithGoogle();
        });

        // Listener to create an account
        signUp.setOnClickListener(view ->
        {
            Intent intent =  new Intent(Login_Page.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Listener for resetting password
        forgotPassword.setOnClickListener(view ->
        {
            Intent intent = new Intent(Login_Page.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }




    private void signInWithGoogle()
    {

    }

    /*

     */
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


}
