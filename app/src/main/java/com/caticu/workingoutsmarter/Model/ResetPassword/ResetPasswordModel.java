package com.caticu.workingoutsmarter.Model.ResetPassword;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordModel
{
    private FirebaseAuth firebaseAuth;

    public ResetPasswordModel() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public Task<Void> resetPassword(String email) {
        return firebaseAuth.sendPasswordResetEmail(email);
    }
}
