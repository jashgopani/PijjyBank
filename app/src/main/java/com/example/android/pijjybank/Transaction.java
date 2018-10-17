package com.example.android.pijjybank;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Transaction {
    String title;
    String category;
    String amount;
    int categoryIcon;
    String date;

    public Transaction(){
        Log.i("default","constructor");
    }

    public Transaction(String mTitle, String mCategory,int mCategoryIcon,String mAmount){
        this.title=mTitle;
        this.category=mCategory;
        this.amount=mAmount;
        DateFormat df = new SimpleDateFormat("dd MMM, yyyy");
        this.date = df.format(Calendar.getInstance().getTime());
        this.categoryIcon = mCategoryIcon;
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
