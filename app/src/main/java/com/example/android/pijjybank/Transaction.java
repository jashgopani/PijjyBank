package com.example.android.pijjybank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Transaction {
    String uid;
    String title;
    String category;
    String amount;
    String mode, party, description;
    String type;//Expense or Income

    String date;

    public String getUid() {
        return uid;
    }

    int categoryIcon;

    public Transaction(String type, String uid, String title, int categoryIcon, String category, String amount, String mode, String party, String description) {
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

    public void setDate(String date) {
        this.date = date;
    }

    public Transaction() {
        Log.i("default", "constructor");
    }

    public String getMode() {
        return mode;
    }

    public String getParty() {
        return party;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public Intent openTransaction(Context context) {

//        String uid;
//        String title;
//        String category;
//        String amount;
//        String mode,party,description;
//        String type;//Expense or Income

        Intent intent = new Intent(context, OpenTransaction.class);
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putString("title", title);
        bundle.putString("category", category);
        bundle.putString("amount", amount);
        bundle.putString("mode", mode);
        bundle.putString("party", party);
        bundle.putString("description", description);
        bundle.putString("type", type);
        bundle.putString("date", date);
        bundle.putInt("categoryIcon", categoryIcon);
        intent.putExtras(bundle);
        return intent;
    }
}
