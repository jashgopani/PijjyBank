package com.example.android.pijjybank;

import android.app.Activity;
import android.app.Instrumentation;

import com.google.firebase.auth.FirebaseAuth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class RegistrationActivityTest {
    @Rule
    public ActivityTestRule<RegistrationActivity> registerRule = new ActivityTestRule<RegistrationActivity>(RegistrationActivity.class);
    private Activity ra;
    private Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(PayrollActivity.class.getName(), null, false);
    private FirebaseAuth fa;

    @Before
    public void setup() {
        fa = FirebaseAuth.getInstance();
        if (fa.getCurrentUser() != null) fa.signOut();
        ra = registerRule.getActivity();
    }

    @Test
    public void RegisterTest() {
        assertNotNull(ra);
        onView(withId(R.id.etRegName)).perform(ViewActions.typeText(Helper.name));
        onView(withId(R.id.etRegUsername)).perform(ViewActions.typeText(Helper.username));
        onView(withId(R.id.etRegPassword)).perform(ViewActions.typeText(Helper.password));
        onView(withId(R.id.etRegConfirmPassword)).perform(ViewActions.typeText(Helper.password));
        closeSoftKeyboard();
        closeSoftKeyboard();
        onView(withId(R.id.registerBtn)).perform(ViewActions.click());
        Activity payroll = getInstrumentation().waitForMonitor(monitor);
        assertNotNull(fa.getCurrentUser());
        assertNotNull(payroll);
        pressBack();

    }

    @After
    public void tearDown() {
        /*Deleting*/
        //delete the test account
//
//        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users");
//        String key = fa.getCurrentUser().getUid();
//        assertNotNull(key);
//        assertNotNull(fa);
//        Espresso.openActionBarOverflowOrOptionsMenu(monitor.getLastActivity());
//        onView(withId(R.id.Logout)).perform(ViewActions.click());
////        fa.signOut();
//        userReference = userReference.child(key);
//        userReference.removeValue();
//
//        FirebaseUser user = fa.getCurrentUser();
//        fa.signInWithEmailAndPassword(Helper.username,Helper.password);
//        user.delete();
//
//        //deletion done and user must be null
//        System.out.println(fa.getCurrentUser().getEmail());

        if (fa.getCurrentUser() != null) fa.signOut();
        ra = null;
    }
}