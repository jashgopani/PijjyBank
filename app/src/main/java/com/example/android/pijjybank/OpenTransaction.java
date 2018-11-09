package com.example.android.pijjybank;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class OpenTransaction extends AppCompatActivity {
    Toolbar toolbar;
    String uid, title, category, amount, mode, party, description, type, date; //Transaction Details
    int categoryIcon;//CategoryIcon
    LinearLayout editTransactionLayout;
    TextView titleTV, categoryTV, amountTV, modeTV, partyTV, descriptionTV, typeTV, dateTV;
    EditText titleET, amountET, partyET, descriptionET;
    private Spinner categorySP, modeSP, currencySP;
    private ArrayList<Category> expenseCategoryArrayList, incomeCategoryArrayList;
    CategoryAdapter categoryAdapter;
    FloatingActionButton editButton, deleteButton, saveEditsButton;
    CardView displayTransactionCardView;
    String key;
    Transaction retrivedObject;

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
                Intent i = new Intent(OpenTransaction.this, EditTransaction.class).putExtras(transactionDetails);
                startActivity(i);
                //goto edit activity
            }
        });


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
        transactionReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Toast.makeText(OpenTransaction.this, "Error 404 : Object not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //Set Onclick Listener for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(OpenTransaction.this)
                        .setTitle("Delete Transaction")
                        .setMessage("Are you sure you want to delete this transaction ?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                transactionReference.child(key).removeValue();
                                Toast.makeText(OpenTransaction.this, "Transaction Deleted Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(OpenTransaction.this, PayrollActivity.class));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(OpenTransaction.this,PayrollActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(OpenTransaction.this, PayrollActivity.class));
        return true;
    }

    private void initializeViews() {
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
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);


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

    private void setTextViews() {
        titleTV.setText(title);
        typeTV.setText(type);
        categoryTV.setText(category);
        amountTV.setText(amount);
        partyTV.setText(party);
        modeTV.setText(mode);
        dateTV.setText(date);

        if (!description.equals("")) {
            descriptionTV.setText(description);
        }


    }


}
