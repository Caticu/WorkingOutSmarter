package com.caticu.workingoutsmarter.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.ViewModel.ResetPassword.ResetPasswordViewModel;

public class ResetPassword extends AppCompatActivity
{
    private TextView email;
    private Button submitButton;
    private ProgressBar progressBar;
    private ResetPasswordViewModel resetPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.EnterEmailTextView);
        submitButton = findViewById(R.id.SubmitButton);
        progressBar = findViewById(R.id.progressBarForgetPassword);
        progressBar.setVisibility(View.INVISIBLE);

        resetPasswordViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(ResetPasswordViewModel.class);

        submitButton.setOnClickListener(view -> {
            String userEmail = email.getText().toString();
            resetPasswordViewModel.resetPassword(userEmail);
        });

        resetPasswordViewModel.getResetSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(ResetPassword.this, "Password reset email was sent to " + email.getText().toString(), Toast.LENGTH_LONG).show();
                submitButton.setClickable(false);
                progressBar.setVisibility(View.INVISIBLE);
                finish();
            }
        });

        resetPasswordViewModel.getResetError().observe(this, error -> {
            if (error) {
                Toast.makeText(ResetPassword.this, "Something went wrong", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        resetPasswordViewModel.getLoading().observe(this, loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.INVISIBLE);
            submitButton.setEnabled(!loading);
        });
    }
}
