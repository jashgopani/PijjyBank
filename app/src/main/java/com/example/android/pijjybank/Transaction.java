package com.example.android.pijjybank;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Transaction {
    String mTitle;
    String mCategory;
    int mAmount;
    Date mDate;
    static DateFormat dateFormat;

    public Transaction(String title, String Category, int amt){
        mTitle=title;
        mCategory=Category;
        mAmount=amt;
        mDate= Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("dd MMM,yyyy");
    }

    public String getTitle(){
        return mTitle;
    }

    public String getCategory(){
        return mCategory;
    }

    public String getAmount(){
        return String.valueOf(mAmount);
    }

    public String getDate(){
        return dateFormat.format(mDate);
    }
}
