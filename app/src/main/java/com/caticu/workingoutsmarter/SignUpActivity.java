package com.caticu.workingoutsmarter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.caticu.workingoutsmarter.Model.User;
import com.caticu.workingoutsmarter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends Activity
{
    EditText name;
    EditText email;
    EditText password;
    Button createAccountButton;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.NameBox);
        email = findViewById(R.id.EmailBox);
        password = findViewById(R.id.PasswordBox);
        createAccountButton = findViewById(R.id.CreateAccountButton);
        progressBar = findViewById(R.id.progressBar);

        //Set the progress bar invisible at first
        progressBar.setVisibility(View.INVISIBLE);


        User user = User.getInstance();
        createAccountButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Create the account only once
                createAccountButton.setClickable(false);

                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userName = name.getText().toString();
                User.getInstance().setName(userName);
                CreateAccountFireBase(userEmail, userPassword);
            }
        });
    }

    /**
     * Create an account using fireBase
     * If succeeded it will give a toast message back and return to the log in page
     * If not,
     * @param email
     * @param password
     */
    public void CreateAccountFireBase(String email, String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(SignUpActivity.this, "The account has been created!", Toast.LENGTH_LONG).show();
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "There was a problem creating your account!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
