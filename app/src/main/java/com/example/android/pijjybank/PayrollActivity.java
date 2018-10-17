package com.example.android.pijjybank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.pijjybank.RegistrationActivity.calledAlready;

public class PayrollActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private DrawerLayout mDrawerLayout;
    private TextView appbarTitle;
    private FloatingActionButton addExpenseBtn;
    private FloatingActionButton addIncomeBtn;
    RecyclerView recyclerView;
    TransactionAdapter expenseAdapter;
    List<Transaction> transactionList;
    DatabaseReference UserRef,TransactionsRef;
    User currentuser;
    String userEmail;
    String id;
    TextView emailSideBar;
    NavigationView navigationView;
    View headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //enabling offline capabilities
        if (!calledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference("Users");
        ValueEventListener nameListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User current = snapshot.getValue(User.class);
                    String key = snapshot.getKey();
                    if(key.equals(id))
                        Toast.makeText(PayrollActivity.this,current.getName(),Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        UserRef.addValueEventListener(nameListener);

        //Transactions retive
        transactionList = new ArrayList<>();
//        transactionList.add(new Transaction("jash","jash",R.drawable.healthcare,123));

        TransactionsRef = FirebaseDatabase.getInstance().getReference("Transactions/Expense");
        ValueEventListener transactionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Transaction temp = snapshot.getValue(Transaction.class);
                    transactionList.add(temp);
                }
                Collections.reverse(transactionList);
                expenseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        TransactionsRef.addValueEventListener(transactionListener);


        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        setContentView(R.layout.activity_payroll);

        //setting up the navigation view
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        if (user != null) {
            // User is signed in
            userEmail = user.getEmail();
            emailSideBar = (TextView)headerView.findViewById(R.id.userEmail);

        }
        emailSideBar.setText(userEmail);

        Toolbar toolbar = findViewById(R.id.payroll_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout = findViewById(R.id.navd);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);

        firebaseAuth = FirebaseAuth.getInstance();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onOptionsItemSelected(item);
                item.setChecked(true);// set item as selected to persist highlight
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        expenseAdapter = new TransactionAdapter(this, (ArrayList<Transaction>) transactionList);

        recyclerView.setAdapter(expenseAdapter);


        //Floating Action Button Event Listeners

        addExpenseBtn = (FloatingActionButton)findViewById(R.id.addExpenseBtn);
        addIncomeBtn =  (FloatingActionButton)findViewById(R.id.addIncomeBtn);

        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PayrollActivity.this,AddExpenseActivity.class));
            }
        });

        addIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PayrollActivity.this,AddIncomeActivity.class));
            }
        });


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
                return true;

            case R.id.Dashboard:
                startActivity(new Intent(PayrollActivity.this,dashboard.class));
                return true;

            case R.id.Profile:
//                finish();
                startActivity(new Intent(PayrollActivity.this,ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        firebaseAuth.signOut();//firebase method
        Log.i("Payroll", "Logout Failed");
        finish();
        startActivity(new Intent(PayrollActivity.this, LoginActivity.class));
        Toast.makeText(this, "Logout Successful ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
