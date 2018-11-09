package com.example.android.pijjybank;

public class User {
    String name;
    int budget;

    public User() {
        //for datasnapshot
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public User(String name, int budget) {
        this.name = name;
        this.budget = budget;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }
}
