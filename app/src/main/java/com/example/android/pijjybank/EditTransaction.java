package com.example.android.pijjybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditTransaction extends AppCompatActivity {

    Toolbar toolbar;
    String uid, title, category, amount, mode, currency, party, description, type, date; //Transaction Details
    int categoryIcon;//CategoryIcon
    EditText titleET, amountET, partyET, descriptionET;
    private Spinner categorySP, modeSP, currencySP;
    private ArrayList<Category> expenseCategoryArrayList, incomeCategoryArrayList;
    CategoryAdapter categoryAdapter;
    FloatingActionButton saveEditsButton;
    Transaction retrivedObject;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);

        //Initialising Toolbar
        toolbar = (Toolbar) findViewById(R.id.edittransaction_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Get Details from PayrollActivity
        final Bundle transactionDetails = getIntent().getExtras();
        uid = transactionDetails.getString("uid");
        title = transactionDetails.getString("title");
        category = transactionDetails.getString("category");
        amount = transactionDetails.getString("amount");
        mode = transactionDetails.getString("mode");
        party = transactionDetails.getString("party");
        description = transactionDetails.getString("description");
        type = transactionDetails.getString("type");//Expense or Income
        date = transactionDetails.getString("date");
        categoryIcon = transactionDetails.getInt("categoryIcon");
        currency = transactionDetails.getString("currency");


        //Setting flag to show only those categories that belong to the type of transaction i.e income / expense spinners
        final boolean isExpense;
        if (type.compareTo("Expense") == 0) {
            isExpense = true;
        } else {
            isExpense = false;
        }

        //setup the activity
        initializeViews(isExpense);


        //getting id of currently logged in user
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final String id = firebaseAuth.getCurrentUser().getUid().trim();

        //Saving the database reference
        final DatabaseReference transactionReference = FirebaseDatabase.getInstance().getReference("Transactions");

        /*
         * If the save button is clicked,
         *   retrive search for the transaction to be edited
         *       while searching match the uid in the transaction and also store the key of the current transaction node
         *       if uid matches current user, then
         * */
        ValueEventListener transactionEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction temp = snapshot.getValue(Transaction.class);
                    key = snapshot.getKey();
                    if (temp.getUid().compareTo(id) == 0) { // if uid matches
                        if (transactionMatched(temp)) {
                            retrivedObject = temp;
                            key = snapshot.getKey();
                            break;
                        }
                    }
                }
                if (retrivedObject == null) {
                    Toast.makeText(EditTransaction.this, "Error 404 : Object not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        transactionReference.addValueEventListener(transactionEventListener);

        //onclick listener for spinner
        categorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//to get Value of Spinner Item
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category clickedItem = (Category) parent.getItemAtPosition(position);
                categoryIcon = clickedItem.getCategoryIcon();
                category = clickedItem.categoryName;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Onclick listener for saving edits
        saveEditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save edits
                if (getAllValues()) {
                    Transaction updated = new Transaction(type, id, title, categoryIcon, category, amount, mode,currency, party, description);
                    updated.setDate(date);
                    transactionReference.child(key).setValue(updated).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(EditTransaction.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(EditTransaction.this, PayrollActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(EditTransaction.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void onBackPressed() {
        new AlertDialog.Builder(EditTransaction.this)
                .setTitle("Discard Changes")
                .setMessage("All unsaved changes will be discarded !")
                .setPositiveButton("Discard Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(EditTransaction.this, PayrollActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        new AlertDialog.Builder(EditTransaction.this)
                .setTitle("Discard Changes")
                .setMessage("All unsaved changes will be discarded ")
                .setPositiveButton("Discard Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(EditTransaction.this, PayrollActivity.class));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
        return true;
    }

    private boolean transactionMatched(Transaction temp) {
        boolean result = false;
        if (temp.getTitle().compareTo(title) == 0) {
            if (temp.getType().compareTo(type) == 0) {
                if (temp.getCategory().compareTo(category) == 0) {
                    if (temp.getAmount().compareTo(amount) == 0) {
                        result = true;
                    }
                }
            }
        }
        return result;

    }

    private void initializeViews(boolean isExpense) {

        //find all buttons
        saveEditsButton = (FloatingActionButton) findViewById(R.id.saveEditsButton);

        //find all edit text views
        titleET = (EditText) findViewById(R.id.editTransactionTitle);
        amountET = (EditText) findViewById(R.id.editTransactionAmount);
        partyET = (EditText) findViewById(R.id.editTransactionParty);
        descriptionET = (EditText) findViewById(R.id.editTransactionDescription);

        //find all spinners
        categorySP = (Spinner) findViewById(R.id.editTransactionCategory);
        modeSP = (Spinner) findViewById(R.id.editTransactionMode);
        currencySP = (Spinner) findViewById(R.id.editTransactionCurrency);

        //Initialise all views with values
        setEditTextViews();
        initAllSpinners(isExpense);

    }

    private void setEditTextViews() {
        titleET.setText(title);
        amountET.setText(amount);
        partyET.setText(party);
        descriptionET.setText(description);
    }

    private void initExpenseCategoryList() {
        expenseCategoryArrayList = new ArrayList<>();
        expenseCategoryArrayList.add(new Category("Food", R.drawable.food));
        expenseCategoryArrayList.add(new Category("Travel", R.drawable.travel));
        expenseCategoryArrayList.add(new Category("Shopping", R.drawable.shopping));
        expenseCategoryArrayList.add(new Category("HealthCare", R.drawable.healthcare));
        expenseCategoryArrayList.add(new Category("Entertainment", R.drawable.entertainment));
        expenseCategoryArrayList.add(new Category("Fees", R.drawable.fees));
        expenseCategoryArrayList.add(new Category("Other", R.drawable.other));
    }

    private void initIncomeCategoryList() {
        incomeCategoryArrayList = new ArrayList<>();
        incomeCategoryArrayList.add(new Category("Salary", R.drawable.salary));
        incomeCategoryArrayList.add(new Category("Gift", R.drawable.gift));
        incomeCategoryArrayList.add(new Category("Depreciation", R.drawable.depreciation));
        incomeCategoryArrayList.add(new Category("Cashback", R.drawable.cashback));
        incomeCategoryArrayList.add(new Category("Prize", R.drawable.prize));
        incomeCategoryArrayList.add(new Category("Other", R.drawable.other));
    }

    private int searchArrayList(ArrayList<Category> categoryList, String element, int number) {
        int position = 0;
        for (Category c : categoryList) {
            if (c.getCategoryName().compareTo(element) == 0 && c.getCategoryIcon() == number) {
                break;
            }
            position++;
        }
        return position%categoryList.size();

    }

    public void initAllSpinners(boolean isExpense) {
        //setup category Spinner
        if (isExpense) { //mode 0 ==> Expense
            initExpenseCategoryList();//initialsing list
            categoryAdapter = new CategoryAdapter(this, expenseCategoryArrayList);//setting up adapter
            categorySP.setAdapter(categoryAdapter);// Apply the adapter to the spinner
            int current = searchArrayList(expenseCategoryArrayList, category, categoryIcon);
            categorySP.setSelection(current, true);


        } else { //mode 1 ==> Income

            initIncomeCategoryList();//initialsing list
            categoryAdapter = new CategoryAdapter(this, incomeCategoryArrayList);//setting up adapter
            categorySP.setAdapter(categoryAdapter);// Apply the adapter to the spinner
            int current = incomeCategoryArrayList.indexOf(category);
            categorySP.setSelection(current, true);
        }

        //setup Mode Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.payment_mode_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSP.setAdapter(adapter2);
        int current2 = getStringItemPosition(mode, R.array.payment_mode_array);
        modeSP.setSelection(current2);
        modeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setup currency spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySP.setAdapter(adapter3);
        Toast.makeText(this, currency, Toast.LENGTH_SHORT).show();
        int ci = getStringItemPosition(currency,R.array.currency_array);
        currencySP.setSelection(ci);
        currencySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currency = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private boolean getAllValues() {
        boolean validity = true;
        title = titleET.getText().toString().trim();
        party = partyET.getText().toString().trim();
        mode = modeSP.getSelectedItem().toString();
        description = descriptionET.getText().toString().trim();
        String temp = amountET.getText().toString();

        if (temp.trim().isEmpty()) {
            amount = "0";
        } else {
            if(currency.equals("USD")){
                int tempAmount = Integer.parseInt(temp);
                tempAmount = tempAmount * 72;
                amount = Integer.toString(tempAmount);
            }else if(currency.equals("UAE")){
                int tempAmount = Integer.parseInt(temp);
                tempAmount = tempAmount * 20;
                amount = Integer.toString(tempAmount);
            }else if(currency.equals("EUR")){
                int tempAmount = Integer.parseInt(temp);
                tempAmount = tempAmount * 82;
                amount = Integer.toString(tempAmount);
            }else if(currency.equals("INR")){
                int tempAmount = Integer.parseInt(temp);
                amount = Integer.toString(tempAmount);
            }
        }

        if (title.isEmpty() || party.isEmpty() || mode.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            validity = false;
        }
        return validity;
    }

    private int getStringItemPosition(String element, int resourceId) {
        int position = 0;
        Resources res = getResources(); //assuming in an activity for example, otherwise you can provide a context.
        String[] array = res.getStringArray(resourceId);
        for (int i = 0; i < array.length; i++) {
            if (array[i].compareTo(element) == 0) {
                position = i;
                break;
            }
        }
        return position;
    }

}
