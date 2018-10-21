package com.example.android.pijjybank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    String tempPassword;
    private DrawerLayout mDrawerLayout;
    private TextView appbarTitle;
    String userEmail;
    TextView emailSideBar;
    NavigationView navigationView;
    View headerView;
    Button resetPassword;
    TextView usernameSideBar;
    DatabaseReference UserRef;
    String navHeaderName;
    String id;
    DatabaseReference TransactionsRef;
    List<Transaction> transactionList;
    PieChart pieChart;
    String [] expenseCategories = {"Food","Travel","Shopping","HealthCare","Entertainment","Fees","Other"};
    String [] incomeCategories = {"Salary","Gift","Depreciation","Cashback","Prize","Other"};
    float expenseSum[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //setting the toolbar
        Toolbar toolbar = findViewById(R.id.dashboard_appbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);


        //Firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = firebaseAuth.getCurrentUser().getUid();
        appbarTitle = (TextView) findViewById(R.id.appbarTitle);
        mDrawerLayout = findViewById(R.id.navd);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onOptionsItemSelected(item);
                item.setChecked(true);// set item as selected to persist highlight
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //setting up the navigation view
        headerView = navigationView.getHeaderView(0);
        if (user != null) {
            // User is signed in
            userEmail = user.getEmail();
            emailSideBar = (TextView) headerView.findViewById(R.id.userEmail);
            usernameSideBar = (TextView) headerView.findViewById(R.id.SidebarHeader);
        }
        emailSideBar.setText(userEmail);

        //Retriving Current Username and updating user details
        UserRef = FirebaseDatabase.getInstance().getReference("Users");
        ValueEventListener nameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User current = snapshot.getValue(User.class);
                    String key = snapshot.getKey();
                    navHeaderName = current.getName();
                    if (key.equals(id)) {
                        navHeaderName = current.getName();
                        usernameSideBar.setText(navHeaderName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        UserRef.addValueEventListener(nameListener);

        //Transactions retive
        transactionList = new ArrayList<>();
        expenseSum = new float[7];
        TransactionsRef = FirebaseDatabase.getInstance().getReference("Transactions");
        ValueEventListener transactionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction temp = snapshot.getValue(Transaction.class);
                    if (temp.uid.equals(id)) {
                        transactionList.add(temp);
                        categorizeExpenses(temp);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        TransactionsRef.addValueEventListener(transactionListener);

        //PieChart Stuff
        pieChart = (PieChart)findViewById(R.id.pieChart);
        Description description = new Description();
        description.setText("Category Wise Distributon");
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(10f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(true);

        addDataSet();
    }

    private void addDataSet(){
        ArrayList<PieEntry> yEntrys= new ArrayList<PieEntry>();//values
        ArrayList<String> xEntrys = new ArrayList<String>();//Category Names

        for(int i=0;i<expenseCategories.length;i++){
            PieEntry ptemp = new PieEntry(i,expenseCategories[i]);
            yEntrys.add(ptemp);
        }

//        for(int i=0;i<expenseCategories.length;i++){
//            xEntrys.add(Float.toString(expenseSum[i]));
//            Log.v("asdfgh","i > "+Float.toString(expenseSum[i]));
//        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys,"Categories");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START, true);
                return true;

            case R.id.Logout:
                logout();
                return true;

            case R.id.Timeline:
                startActivity(new Intent(dashboard.this,PayrollActivity.class));
                return true;

            case R.id.Dashboard:
                return true;

            case R.id.Profile:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        firebaseAuth.signOut();//firebase method
        finish();
        startActivity(new Intent(dashboard.this, LoginActivity.class));
        Toast.makeText(this, "Logout Successful ", Toast.LENGTH_SHORT).show();
    }

    public void categorizeExpenses(Transaction t){
        float val = Integer.parseInt(t.getAmount());
        Log.v("asdfgh",Float.toString(val));
        if(t.type=="Expense"){
            if(t.categoryIcon==R.drawable.food){
                expenseSum[0] = expenseSum[0] + val;
                Log.v("expenseSum",Float.toString(expenseSum[0]));
            }else if(t.category=="Travel"){
                expenseSum[1] += val;
            }else if(t.category=="Shopping"){
                expenseSum[2] += val;
            }else if(t.category=="HealthCare"){
                expenseSum[3] += val;
            }else if(t.category=="Entertainment"){
                expenseSum[4] += val;
            }else if(t.category=="Fees"){
                expenseSum[5] += val;
            }else if(t.category=="Other"){
                expenseSum[6] += val;
            }else{
                Toast.makeText(this, "Some other error", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
