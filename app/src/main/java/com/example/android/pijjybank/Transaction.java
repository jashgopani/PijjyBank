package com.example.android.pijjybank;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Transaction {
    String uid;
    String title;
    String category;
    String amount;
    String mode,party,description;
    String type;//Expense or Income

    public Transaction(String type,String uid, String title, int categoryIcon,String category, String amount, String mode, String party, String description) {
        this.type = type;
        this.uid = uid;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.mode = mode;
        this.party = party;
        this.description = description;
        DateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        this.date = df.format(Calendar.getInstance().getTime());
        this.categoryIcon = categoryIcon;
    }

    int categoryIcon;
    String date;

    public Transaction(){
        Log.i("default","constructor");
    }

    public String getTitle(){
        return title;
    }

    public String getCategory(){
        return category;
    }

    public String getAmount(){
        return amount;
    }

    public String getDate(){
        return date;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }
}
