package com.example.android.pijjybank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddIncomeActivity extends AppCompatActivity {
    DatabaseReference database;
    Button save;
    private Spinner category, mode, currencyType;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryArrayList;
    String expenseCategory;
    int categoryIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        Toolbar toolbar = findViewById(R.id.addexpense_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //spinner wala stuff
        initList();//initialsing list
        category = (Spinner) findViewById(R.id.incomeCategory);
        categoryAdapter = new CategoryAdapter(this,categoryArrayList);//setting up adapter
        category.setAdapter(categoryAdapter);// Apply the adapter to the spinner
        category.setSelection(0);
        category.setPrompt("Khali");


        mode = (Spinner) findViewById(R.id.incomeMode);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.incomeCategory_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(adapter2);

        currencyType = (Spinner) findViewById(R.id.incomeCurrency);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.incomeCategory_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyType.setAdapter(adapter3);

        //saving reference of database
        database = FirebaseDatabase.getInstance().getReference();

        save=(Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction t = new Transaction("Nibba","Expensive",5,"6969");
                DatabaseReference child = database.child("Transactions/Income");
                child.push().setValue(t);
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

    public  void initList(){
        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category("Salary",R.drawable.salary));
        categoryArrayList.add(new Category("Gift",R.drawable.gift));
        categoryArrayList.add(new Category("Depreciation",R.drawable.depreciation));
        categoryArrayList.add(new Category("Cashback",R.drawable.cashback));
        categoryArrayList.add(new Category("Prize",R.drawable.prize));
        categoryArrayList.add(new Category("Other",R.drawable.other));
    }
}
