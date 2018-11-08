package com.example.android.pijjybank;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class OpenTransaction extends AppCompatActivity {
    Toolbar toolbar;
    String uid,title,category,amount,mode,party,description,type,date; //Transaction Details
    int categoryIcon;//CategoryIcon
    LinearLayout editTransactionLayout;
    TextView titleTV,categoryTV,amountTV,modeTV,partyTV,descriptionTV,typeTV,dateTV;
    EditText titleET,amountET,partyET,descriptionET;
    private Spinner categorySP, modeSP, currencySP;
    private ArrayList<Category> expenseCategoryArrayList,incomeCategoryArrayList;
    CategoryAdapter categoryAdapter;
    Button editButton,deleteButton,saveEditsButton,cancelEditsButton;
    CardView displayTransactionCardView;
    String categoryValue,modeValue,currencyValue;
    int categoryIconValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_transaction);

        //Initialising Toolbar
        toolbar = (Toolbar) findViewById(R.id.opentransaction_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Get Details from PayrollActivity
        Bundle transactionDetails = getIntent().getExtras();
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


        //Setup the Activity
        initializeViews();
        setTextViews();

        final int modeNumber;
        if(category.compareTo("Expense")==0){
            modeNumber = 0;
        }else{
            modeNumber = 1;
        }

        //OnclickListener For Edit Button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditingLayout(modeNumber);
            }
        });

        //OnClickListener for SaveEdits Button
        saveEditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Transactions/");

            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(OpenTransaction.this, PayrollActivity.class));
        return true;
    }

    public void onBackPressed() {
        finish();
        startActivity(new Intent(OpenTransaction.this, PayrollActivity.class));
        super.onBackPressed();
    }

    private void initializeViews(){
        //find all the text views
        titleTV = (TextView) findViewById(R.id.displayTitle);
        typeTV = (TextView) findViewById(R.id.displayType);
        categoryTV = (TextView) findViewById(R.id.displayCategory);
        amountTV = (TextView) findViewById(R.id.displayAmount);
        partyTV = (TextView) findViewById(R.id.displayParty);
        modeTV = (TextView) findViewById(R.id.displayMode);
        dateTV = (TextView) findViewById(R.id.displayDate);
        descriptionTV = (TextView) findViewById(R.id.displayDescription);

        //find all buttons
        editButton = (Button)findViewById(R.id.editButton);
        deleteButton = (Button)findViewById(R.id.deleteButton);
        saveEditsButton = (Button)findViewById(R.id.saveEditsButton);
        cancelEditsButton = (Button)findViewById(R.id.cancelEditsButton);

        //hide the editing layout
        editTransactionLayout = (LinearLayout) findViewById(R.id.editTransactionLayout);
        editTransactionLayout.setVisibility(LinearLayout.GONE);

        //find the main card view
        displayTransactionCardView = (CardView) findViewById(R.id.displayTransactionCardView);

        //find all edit text views
        titleET = (EditText) findViewById(R.id.editTransactionTitle);
        amountET = (EditText) findViewById(R.id.editTransactionAmount);
        partyET = (EditText) findViewById(R.id.editTransactionParty);
        descriptionET = (EditText) findViewById(R.id.editTransactionDescription);

        //find all spinners
        categorySP = (Spinner) findViewById(R.id.editTransactionCategory);
        modeSP = (Spinner) findViewById(R.id.editTransactionMode);
        currencySP = (Spinner) findViewById(R.id.editTransactionCurrency);

    }

    private void setTextViews(){
        titleTV.setText(title);
        typeTV.setText(type);
        categoryTV.setText(category);
        amountTV.setText(amount);
        partyTV.setText(party);
        modeTV.setText(mode);
        dateTV.setText(date);

        if(!description.equals("")){
            descriptionTV.setText(description);
        }


    }

    private void setEditTextViews(){
        titleET.setText(title);
        amountET.setText(amount);
        partyET.setText(party);
        descriptionET.setText(description);
    }

    private void showEditingLayout(int modeNumber){
        //hide cardview
        displayTransactionCardView.setVisibility(CardView.GONE);

        //show editing layout
        editTransactionLayout.setVisibility(LinearLayout.VISIBLE);

        //setup all elements
        setEditTextViews();
        initAllSpinners(modeNumber);
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

    private void initIncomeCategoryList(){
        incomeCategoryArrayList = new ArrayList<>();
        incomeCategoryArrayList.add(new Category("Salary",R.drawable.salary));
        incomeCategoryArrayList.add(new Category("Gift",R.drawable.gift));
        incomeCategoryArrayList.add(new Category("Depreciation",R.drawable.depreciation));
        incomeCategoryArrayList.add(new Category("Cashback",R.drawable.cashback));
        incomeCategoryArrayList.add(new Category("Prize",R.drawable.prize));
        incomeCategoryArrayList.add(new Category("Other",R.drawable.other));
    }

    public void initAllSpinners(int modeNumber){
        //setup category Spinner
        if(modeNumber == 0){ //mode 0 ==> Expense
            initExpenseCategoryList();//initialsing list
            categoryAdapter = new CategoryAdapter(this, expenseCategoryArrayList);//setting up adapter
            categorySP.setAdapter(categoryAdapter);// Apply the adapter to the spinner
            int current = expenseCategoryArrayList.indexOf(category);
            categorySP.setSelection(current,true);
            categorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//to get Value of Spinner Item
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

        }else{ //mode 1 ==> Income

            initIncomeCategoryList();//initialsing list
            categoryAdapter = new CategoryAdapter(this, incomeCategoryArrayList);//setting up adapter
            categorySP.setAdapter(categoryAdapter);// Apply the adapter to the spinner
            int current = incomeCategoryArrayList.indexOf(category);
            categorySP.setSelection(current,true);
            categorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {//to get Value of Spinner Item
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

        }

        //setup Mode Spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.payment_mode_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSP.setAdapter(adapter2);
        int current2 = getStringItemPosition(mode,R.array.payment_mode_array);
        modeSP.setSelection(current2);
        modeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modeValue = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setup currency spinner
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySP.setAdapter(adapter3);
        currencySP.setSelection(0);
        currencySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyValue = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private int getStringItemPosition(String element,int resourceId){
        int position =0 ;
        Resources res = getResources(); //assuming in an activity for example, otherwise you can provide a context.
        String[] array = res.getStringArray(resourceId);
        for(int i=0;i<array.length;i++){
            if(array[i].compareTo(element)==0){
                position = i;
                break;
            }
        }
        return position;
    }
}
