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

public class AddExpenseActivity extends AppCompatActivity {
    DatabaseReference database;
    Button save;
    private Spinner category, mode, currencyType;
    static int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        count=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        Toolbar toolbar = findViewById(R.id.addexpense_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //spinner wala stuff
        category = (Spinner) findViewById(R.id.expenseCategory);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,R.array.expenseCategory_array, android.R.layout.simple_spinner_item);// Create an ArrayAdapter using the string array and a default spinner layout
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);// Specify the layout to use when the list of choices appears
        category.setAdapter(adapter1);// Apply the adapter to the spinner


        mode = (Spinner) findViewById(R.id.expenseMode);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.expenseCategory_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(adapter2);

        currencyType = (Spinner) findViewById(R.id.expenseCurrency);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.expenseCategory_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyType.setAdapter(adapter3);

        database = FirebaseDatabase.getInstance().getReference("https://pijjy-bank.firebaseio.com/");


        save=(Button)findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(AddExpenseActivity.this, PayrollActivity.class));
        return true;
    }

    public void onBackPressed() {
        finish();
        startActivity(new Intent(AddExpenseActivity.this, PayrollActivity.class));
        super.onBackPressed();
    }
}
