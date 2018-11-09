package com.example.android.pijjybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    DatabaseReference UserRef, TransactionsRef;
    String navHeaderName;
    String userEmail;
    String id;
    TextView emailSideBar, usernameSideBar;
    NavigationView navigationView;
    View headerView;
    LinearLayout zeroTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payroll);

        //ZeroTransactions TextView
        zeroTransactions = (LinearLayout) findViewById(R.id.zeroTransactions);

        //enabling offline capabilities
        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        //getting name of currently logged in user
        firebaseAuth = FirebaseAuth.getInstance();
        id = firebaseAuth.getCurrentUser().getUid().trim();


        //Getting FirebaseAuth User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //setting up the navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        if (user != null) {
            // User is signed in
            userEmail = user.getEmail();
            emailSideBar = (TextView) headerView.findViewById(R.id.userEmail);
            usernameSideBar = (TextView) headerView.findViewById(R.id.SidebarHeader);
        }
        emailSideBar.setText(userEmail);

        //Retriving Current Username
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
                        if (current.getBudget() == 0) {
                            getBudget(navHeaderName);
                        }
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
        TransactionsRef = FirebaseDatabase.getInstance().getReference("Transactions");
        ValueEventListener transactionListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction temp = snapshot.getValue(Transaction.class);
                    if (temp.uid.equals(id)) {
                        transactionList.add(temp);
                    }
                }
                Collections.reverse(transactionList);
                expenseAdapter.notifyDataSetChanged();
                if (transactionList.size() == 0) {
                    zeroTransactions.setVisibility(LinearLayout.VISIBLE);
                } else {
                    zeroTransactions.setVisibility(LinearLayout.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        TransactionsRef.addValueEventListener(transactionListener);


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


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.reverse(transactionList);
        expenseAdapter = new TransactionAdapter(this, (ArrayList<Transaction>) transactionList);

        recyclerView.setAdapter(expenseAdapter);

        expenseAdapter.setOnItemClickListener(new TransactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = transactionList.get(position).openTransaction(PayrollActivity.this);
                finish();
                startActivity(intent);
            }
        });

        //Floating Action Button Event Listeners

        addExpenseBtn = (FloatingActionButton) findViewById(R.id.addExpenseBtn);
        addIncomeBtn = (FloatingActionButton) findViewById(R.id.addIncomeBtn);

        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PayrollActivity.this, AddExpenseActivity.class));
            }
        });

        addIncomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(PayrollActivity.this, AddIncomeActivity.class));
            }
        });

//        usernameSideBar.setText(currentUserName);

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
                startActivity(new Intent(PayrollActivity.this, dashboard.class));
                return true;

            case R.id.Profile:
                finish();
                startActivity(new Intent(PayrollActivity.this, ProfileActivity.class));
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

    public void getBudget(final String username) {
        final int[] userbudget = new int[1];
        //Input Dialog Box For getting password
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Monthly Budget");
        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String x = input.getText().toString();
                userbudget[0] = Integer.parseInt(x);

                final String id = firebaseAuth.getCurrentUser().getUid();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users/");
                db.child(id).setValue(new User(username, userbudget[0]));
            }
        });
        builder.show();
    }

}
