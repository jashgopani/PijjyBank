package com.example.android.pijjybank;

public class User {
    String name;

    public User(){
        //for datasnapshot
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
