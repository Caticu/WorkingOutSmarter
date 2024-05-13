package com.caticu.workingoutsmarter.Model.Authentication;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationModel
{
    private FirebaseAuth firebaseAuth;

    public AuthenticationModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> signInWithEmailPassword(String email, String password) {
        return firebaseAuth.signInWithEmailAndPassword(email, password);
    }

}
