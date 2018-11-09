package com.example.android.pijjybank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddIncomeActivity extends AppCompatActivity {
    DatabaseReference database;
    Button save;
    FirebaseAuth firebaseAuth;
    private Spinner category, mode, currencyType;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryArrayList;
    String expenseCategory;
    int categoryIcon;
    private String titleValue, categoryValue, amountValue, modeValue, payerValue, descriptionValue, currencyTypeValue;
    private int categoryIconValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        Toolbar toolbar = findViewById(R.id.addincome_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //spinner wala stuff
        initList();//initialsing list
        category = (Spinner) findViewById(R.id.incomeCategory);
        categoryAdapter = new CategoryAdapter(this, categoryArrayList);//setting up adapter
        category.setAdapter(categoryAdapter);// Apply the adapter to the spinner
        category.setSelection(0);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//to get Value of Spinner Item
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category clickedItem = (Category) parent.getItemAtPosition(position);
                categoryIconValue = clickedItem.getCategoryIcon();
                categoryValue = clickedItem.getCategoryName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mode = (Spinner) findViewById(R.id.incomeMode);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.payment_mode_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(adapter2);
        mode.setSelection(0);
        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modeValue = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddIncomeActivity.this, "Mode can't be empty", Toast.LENGTH_SHORT).show();
            }
        });


        currencyType = (Spinner) findViewById(R.id.incomeCurrency);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyType.setAdapter(adapter3);
        currencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyTypeValue = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddIncomeActivity.this, "Currency Type can't be empty", Toast.LENGTH_SHORT).show();
            }
        });

        //saving reference of database
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //get User Id
        final String userID = firebaseAuth.getCurrentUser().getUid();

        //saving reference of database
        database = FirebaseDatabase.getInstance().getReference();

        save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAllValues()) {
                    Transaction t = new Transaction("Income", userID, titleValue, categoryIconValue, categoryValue, amountValue, modeValue, payerValue, descriptionValue);
                    DatabaseReference child = database.child("Transactions");
                    child.push().setValue(t);
                    Toast.makeText(AddIncomeActivity.this, "Income Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(AddIncomeActivity.this, PayrollActivity.class));
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(AddIncomeActivity.this, PayrollActivity.class));
        return true;
    }

    public void onBackPressed() {
        finish();
        startActivity(new Intent(AddIncomeActivity.this, PayrollActivity.class));
        super.onBackPressed();
    }

    public void initList() {
        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category("Salary", R.drawable.salary));
        categoryArrayList.add(new Category("Gift", R.drawable.gift));
        categoryArrayList.add(new Category("Depreciation", R.drawable.depreciation));
        categoryArrayList.add(new Category("Cashback", R.drawable.cashback));
        categoryArrayList.add(new Category("Prize", R.drawable.prize));
        categoryArrayList.add(new Category("Other", R.drawable.other));
    }

    private boolean getAllValues() {
        boolean validity = true;
        titleValue = ((EditText) findViewById(R.id.incomeTitle)).getText().toString().trim();
        payerValue = ((EditText) findViewById(R.id.incomePayer)).getText().toString().trim();
        modeValue = mode.getSelectedItem().toString();
        currencyTypeValue = currencyType.getSelectedItem().toString();
        descriptionValue = ((EditText) findViewById(R.id.incomeDescription)).getText().toString().trim();
        String temp = ((EditText) findViewById(R.id.incomeAmount)).getText().toString();

        if (temp.trim().isEmpty()) {
            amountValue = "0";
        } else {
            amountValue = temp;
        }

        if (titleValue.isEmpty() || payerValue.isEmpty() || modeValue.isEmpty() || currencyTypeValue.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        return validity;
    }
}
