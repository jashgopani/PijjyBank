package com.example.android.pijjybank;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    private String titleValue, categoryValue, modeValue, payeeValue, descriptionValue, currencyTypeValue;
    private int amountValue, categoryIconValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //Initialising Toolbar
        toolbar = findViewById(R.id.addexpense_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //spinner wala stuff
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

        mode = (Spinner) findViewById(R.id.expenseMode);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.payment_mode_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(adapter2);

        currencyType = (Spinner) findViewById(R.id.expenseCurrency);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyType.setAdapter(adapter3);


        //saving reference of database
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        //get User Id
        final String userID = firebaseAuth.getCurrentUser().getUid();
        Query query = database.child("Users").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("qpr"+dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("DATABASE",databaseError.getMessage());
            }
        });

        save = (Button) findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllValues();
                Transaction t = new Transaction(titleValue, categoryValue, categoryIconValue, String.valueOf(amountValue));
                DatabaseReference child = database.child("Transactions/Expense");
                child.push().setValue(t);
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

    private void getAllValues() {
        titleValue = ((EditText) findViewById(R.id.expenseTitle)).getText().toString();
        payeeValue = ((EditText) findViewById(R.id.expensePayee)).getText().toString();
        modeValue = mode.getSelectedItem().toString();
        currencyTypeValue = currencyType.getSelectedItem().toString();
        descriptionValue = ((EditText) findViewById(R.id.expenseDescription)).getText().toString();
        String temp = ((EditText) findViewById(R.id.expenseAmount)).getText().toString();
        if (temp.isEmpty()) {
            amountValue = 0;
        } else {
            amountValue = Integer.parseInt(temp);
        }

        Log.v("abc", "titleValue " + titleValue);
        Log.v("abc", "payeeValue " + payeeValue);
        Log.v("abc", "modeValue " + modeValue);
        Log.v("abc", "descriptionValue " + descriptionValue);
        Log.v("abc", "amountValue " + amountValue);
    }
}
