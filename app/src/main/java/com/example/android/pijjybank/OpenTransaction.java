package com.example.android.pijjybank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

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
    FloatingActionButton editButton,deleteButton,saveEditsButton;
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


        //Setup the Activity
        initializeViews();
        setTextViews();


        //OnclickListener For Edit Button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OpenTransaction.this,EditTransaction.class).putExtras(transactionDetails);
                startActivity(i);
                //goto edit activity
            }
        });



    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(OpenTransaction.this, PayrollActivity.class));
        return true;
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
        editButton = (FloatingActionButton) findViewById(R.id.editButton);
        deleteButton = (FloatingActionButton)findViewById(R.id.deleteButton);


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



}
