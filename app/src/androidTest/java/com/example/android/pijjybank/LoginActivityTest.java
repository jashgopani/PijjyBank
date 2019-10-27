package com.example.android.pijjybank;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class LoginActivityTest {
    @Rule
    public ActivityTestRule<LoginActivity> mLoginTestRule = new ActivityTestRule<>(LoginActivity.class);

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(PayrollActivity.class.getName(), null, false);
    FirebaseAuth fa;
    private Activity loginActivity;

    @Before
    public void setUp() {
        fa = FirebaseAuth.getInstance();
        if (fa.getCurrentUser()!=null)fa.signOut();
        loginActivity = mLoginTestRule.getActivity();
    }

    @Test
    public void LoginTest() {
        assertNotNull(loginActivity);
        onView(withId(R.id.etUsername)).perform(ViewActions.typeText(Helper.username));
        onView(withId(R.id.etPassword)).perform(ViewActions.typeText(Helper.password));
        closeSoftKeyboard();
        onView(withId(R.id.loginBtn)).perform(ViewActions.click());
        getInstrumentation().waitForMonitorWithTimeout(monitor, 15000);
        assertNotNull(fa);
    }

    @After
    public void tearDown() {
        if (fa.getCurrentUser()!=null)fa.signOut();
        loginActivity = null;
    }
}