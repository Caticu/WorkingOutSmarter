package com.caticu.workingoutsmarter.View.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.caticu.workingoutsmarter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity
{
    TextView email;
    Button submitButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.EnterEmailTextView);
        submitButton = findViewById(R.id.SubmitButton);
        progressBar = findViewById(R.id.progressBarForgetPassword);
        progressBar.setVisibility(View.INVISIBLE);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String userEmail = email.getText().toString();
                resetPassword(userEmail);
            }
        });
    }

    /**
     * Reset the password using firebase
     * @param email
     */
    public void resetPassword(String email)
    {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                progressBar.setVisibility(View.VISIBLE);
                if(task.isSuccessful())
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Password reset email was sent to " + email, Toast.LENGTH_LONG).show();
                    submitButton.setClickable(false);
                    progressBar.setVisibility(View.INVISIBLE);

                    finish();
                }
                else
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });


    }
}