package com.example.android.pijjybank;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.format;

public class PayrollActivityTest {
    @Rule
    public ActivityTestRule<PayrollActivity> mPayrollTestRule = new ActivityTestRule<>(PayrollActivity.class);

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(AddExpenseActivity.class.getName(), null, false);
    FirebaseAuth fa;
    private Activity payrollActivity;

    @Before
    public void setUp() throws Exception {
        fa = FirebaseAuth.getInstance();
        assertNotNull(fa.getCurrentUser());
        payrollActivity = mPayrollTestRule.getActivity();

    }


    @Test
    public void Rt(){
        assertNotNull(payrollActivity);
        addExpense();
    }

    public void addExpense(){
//        Transaction t = new Transaction("Expense", userID, titleValue, categoryIconValue, categoryValue, amountValue,currencyTypeValue, modeValue, payeeValue, descriptionValue);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Transaction t = new Transaction("Expense", fa.getCurrentUser().getUid(), "STUB EXPENSE", 2131230896, "Travel", "300", "INR","Cash", "OTHER PERSON", "TEST TEST TEST");
        DatabaseReference child = database.child("Transactions");
        child.push().setValue(t);
//        Toast.makeText(payrollActivity, "Expense Added Successfully", Toast.LENGTH_SHORT).show();
    }

    @After
    public void tearDown() throws Exception {
//        payrollActivity = null;
    }
}