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

public class AddExpenseActivity extends AppCompatActivity {
    DatabaseReference database;
    FirebaseAuth firebaseAuth;
    Button save;
    private Spinner category, mode, currencyType;
    private CategoryAdapter categoryAdapter;
    private ArrayList<Category> categoryArrayList;
    String expenseCategory;
    int categoryIcon;
    Toolbar toolbar;
    private String titleValue, categoryValue, amountValue, modeValue, payeeValue, descriptionValue, currencyTypeValue;
    private int categoryIconValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //Initialising Toolbar
        toolbar = findViewById(R.id.addexpense_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Category spinner wala stuff
        initList();//initialsing list
        category = (Spinner) findViewById(R.id.expenseCategory);
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

        //Expense Mode spinner
        mode = (Spinner) findViewById(R.id.expenseMode);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.payment_mode_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(adapter2);
        mode.setSelection(0);
        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modeValue = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddExpenseActivity.this, "Mode can't be empty", Toast.LENGTH_SHORT).show();
            }
        });

        //Currency Type
        currencyType = (Spinner) findViewById(R.id.expenseCurrency);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyType.setAdapter(adapter3);
        currencyType.setSelection(0);
        currencyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyTypeValue = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddExpenseActivity.this, "Currency Type can't be empty", Toast.LENGTH_SHORT).show();
            }
        });


        //saving reference of database
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //get User Id
        final String userID = firebaseAuth.getCurrentUser().getUid();


        save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getAllValues()){
                    Transaction t = new Transaction("Expense",userID,titleValue,categoryIconValue,categoryValue,amountValue,modeValue,payeeValue,descriptionValue);
                    DatabaseReference child = database.child("Transactions");
                    child.push().setValue(t);
                    Toast.makeText(AddExpenseActivity.this, "Expense Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(AddExpenseActivity.this, PayrollActivity.class));
                }
            }
        });

    }

    public void initList() {
        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(new Category("Food", R.drawable.food));
        categoryArrayList.add(new Category("Travel", R.drawable.travel));
        categoryArrayList.add(new Category("Shopping", R.drawable.shopping));
        categoryArrayList.add(new Category("HealthCare", R.drawable.healthcare));
        categoryArrayList.add(new Category("Entertainment", R.drawable.entertainment));
        categoryArrayList.add(new Category("Fees", R.drawable.fees));
        categoryArrayList.add(new Category("Other", R.drawable.other));
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

    private boolean getAllValues() {
        boolean validity = true;
        titleValue = ((EditText) findViewById(R.id.expenseTitle)).getText().toString().trim();
        payeeValue = ((EditText) findViewById(R.id.expensePayee)).getText().toString().trim();
        modeValue = mode.getSelectedItem().toString();
        currencyTypeValue = currencyType.getSelectedItem().toString();
        descriptionValue = ((EditText) findViewById(R.id.expenseDescription)).getText().toString().trim();
        String temp = ((EditText) findViewById(R.id.expenseAmount)).getText().toString();

        if (temp.trim().isEmpty()) {
            amountValue = "0";
        } else {
            amountValue = temp;
        }

        if(titleValue.isEmpty() || payeeValue.isEmpty() || modeValue.isEmpty() || currencyTypeValue.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        return validity;
    }
}
