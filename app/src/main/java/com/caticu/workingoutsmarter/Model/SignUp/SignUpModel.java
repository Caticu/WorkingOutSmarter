package com.caticu.workingoutsmarter.Model.SignUp;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpModel
{
    private FirebaseAuth firebaseAuth;

    public SignUpModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        return firebaseAuth.createUserWithEmailAndPassword(email, password);
    }
}
