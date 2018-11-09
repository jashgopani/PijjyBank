package com.example.android.pijjybank;

import android.content.pm.ActivityInfo;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.LinearLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PayrollActivityTest {

    @Rule
    public ActivityTestRule<PayrollActivity> mActivityTestRule = new ActivityTestRule<PayrollActivity>(PayrollActivity.class);

    private PayrollActivity pact = null;

    @Before
    public void setUp() throws Exception {
        pact = mActivityTestRule.getActivity();
    }

    @Test
    public void testActivity(){
        LinearLayout toolbar = (LinearLayout)pact.findViewById(R.id.zeroTransactions);
        assertNotNull(toolbar);
    }

    @Test
    public void testTransactions(){
        assertNotNull(pact.transactionList);
    }

    @Test
    public void testRecycler(){
        View view = pact.findViewById(R.id.recyclerView);
        assertNotNull(view);
    }

    @Test
    public void testAdapter(){
        assertNotNull(pact.expenseAdapter);
    }

    @Test
    public void testCursor(){
        int orientation;
        orientation = pact.getRequestedOrientation();
        assertEquals(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,orientation);
    }

    @After
    public void tearDown() throws Exception {
        pact = null;
    }
}