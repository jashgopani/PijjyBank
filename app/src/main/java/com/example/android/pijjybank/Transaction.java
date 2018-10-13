package com.example.android.pijjybank;

public class Transaction {
    String title;
    String category;
    int amount;
    String date;
    public Transaction(){

    }

    public Transaction(String mTitle, String mCategory, int mAmount, String mDate){
        this.title=mTitle;
        this.category=mCategory;
        this.amount=mAmount;
        this.date = mDate;

    }

    public String getTitle(){
        return title;
    }

    public String getCategory(){
        return category;
    }

    public String getAmount(){
        return String.valueOf(amount);
    }

    public String getDate(){
        return date;
    }


}
