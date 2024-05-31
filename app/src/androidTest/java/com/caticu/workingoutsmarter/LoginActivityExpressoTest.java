package com.caticu.workingoutsmarter;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.caticu.workingoutsmarter.R;
import com.caticu.workingoutsmarter.View.Activities.ForgotPasswordActivity;
import com.caticu.workingoutsmarter.View.Activities.LoginActivity;
import com.caticu.workingoutsmarter.View.Activities.SignUpActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class LoginActivityExpressoTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void GoesToForgetPasswordActivityOnClick() {

        Espresso.onView(ViewMatchers.withId(R.id.ForgotPasswordTextView)).perform(ViewActions.click());

        // Check if password activity
        Intents.intended(IntentMatchers.hasComponent(ForgotPasswordActivity.class.getName()));
    }

    @Test
    public void GoesToSignUpActivityOnClick() {

        Espresso.onView(ViewMatchers.withId(R.id.NoAccountTextView)).perform(ViewActions.click());

        // Check if signUp
        Intents.intended(IntentMatchers.hasComponent(SignUpActivity.class.getName()));
    }


}
