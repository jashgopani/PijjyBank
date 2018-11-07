package com.example.android.pijjybank;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    PieChart expensePieChart,incomePieChart;
    String [] expenseCategories = {"Food","Travel","Shopping","HealthCare","Entertainment","Fees","Other"};
    String [] incomeCategories = {"Salary","Gift","Depreciation","Cashback","Prize","Other"};
    float expenseSum[];
    float incomeSum[];

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

        //PieChart Stuff
        //set properties of pie chart
        expensePieChart = (PieChart)findViewById(R.id.expensePieChart);
        incomePieChart = (PieChart)findViewById(R.id.incomePieChart);


        //Transactions retrieve
        transactionList = new ArrayList<>();
        expenseSum = new float[7];
        incomeSum = new float[6];
        TransactionsRef = FirebaseDatabase.getInstance().getReference("Transactions");
        ValueEventListener transactionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction temp = snapshot.getValue(Transaction.class);
                    if (temp.uid.equals(id)) {
                        transactionList.add(temp);
                        categorizeExpenses(temp);
                        drawPieChart(expensePieChart,"Expense\nSummary",expenseCategories,expenseSum,"Expense Categories",ColorTemplate.COLORFUL_COLORS);
                        drawPieChart(incomePieChart,"Income\nSummary",incomeCategories,incomeSum,"Income Categories",ColorTemplate.MATERIAL_COLORS);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        TransactionsRef.addValueEventListener(transactionListener);

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
                startActivity(new Intent(dashboard.this,ProfileActivity.class));
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
//        Toast.makeText(this, t.getType(), Toast.LENGTH_SHORT).show();
        String type = t.getType();
        String category = t.getCategory();
        if(type.compareTo("Expense") == 0){
            if(category.compareTo(expenseCategories[0]) == 0){
                expenseSum[0] += val;
            }else if(category.compareTo(expenseCategories[1]) == 0){
                expenseSum[1] += val;
            }else if(category.compareTo(expenseCategories[2]) == 0){
                expenseSum[2] += val;
            }else if(category.compareTo(expenseCategories[3]) == 0){
                expenseSum[3] += val;
            }else if(category.compareTo(expenseCategories[4]) == 0){
                expenseSum[4] += val;
            }else if(category.compareTo(expenseCategories[5]) == 0){
                expenseSum[5] += val;
            }else if(category.compareTo(expenseCategories[6]) == 0){
                expenseSum[6] += val;
            }else{
                Toast.makeText(this, "Some other error", Toast.LENGTH_SHORT).show();
            }
        }else{
//            Toast.makeText(this, "Income", Toast.LENGTH_SHORT).show();
            if(category.compareTo(incomeCategories[0]) == 0){
                incomeSum[0] += val;
            }else if(category.compareTo(incomeCategories[1]) == 0){
                incomeSum[1] += val;
            }else if(category.compareTo(incomeCategories[2]) == 0){
                incomeSum[2] += val;
            }else if(category.compareTo(incomeCategories[3]) == 0){
                incomeSum[3] += val;
            }else if(category.compareTo(incomeCategories[4]) == 0){
                incomeSum[4] += val;
            }else if(category.compareTo(incomeCategories[5]) == 0){
                incomeSum[5] += val;
            }else{
                Toast.makeText(this, "Some other error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void drawPieChart(PieChart p ,String chartTitle, String[] categoryNames,float[] categoryValues,String legendLabel,int[] colorTemplate){
        p.setUsePercentValues(false);
        p.getDescription().setEnabled(false);
        p.setDragDecelerationFrictionCoef(0.95f);
        p.setDrawHoleEnabled(true);
        p.setHoleColor(Color.WHITE);
        p.setTransparentCircleRadius(50f);
        p.setClickable(true);

        //Create Entries for pie chart
        ArrayList<PieEntry> yValues = new ArrayList<>();
        for(int i=0;i<categoryValues.length;i++){
            if(categoryValues[i]>0)
                yValues.add(new PieEntry(categoryValues[i],categoryNames[i]));
//            Toast.makeText(this, expenseSum[i]+" / "+expenseCategories[i], Toast.LENGTH_SHORT).show();
        }

        //Generate a dataset using those entries
        PieDataSet dataSet = new PieDataSet(yValues,legendLabel);
        dataSet.setSelectionShift(10f);
        dataSet.setColors(colorTemplate);
        dataSet.setValueLineColor(Color.YELLOW);

        //set that dataset as the data source of the piechart
        PieData data = new PieData(dataSet);
        data.setValueTextSize(15f);
        data.setHighlightEnabled(true);
        data.setValueTextColor(Color.WHITE);
        p.setCenterText(chartTitle);
        p.setDrawEntryLabels(false);
        p.setData(data);
        p.setEntryLabelColor(Color.WHITE);

        p.getLegend().setWordWrapEnabled(true);
        p.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
    }
}
