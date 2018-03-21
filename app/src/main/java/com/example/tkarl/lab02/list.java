package com.example.tkarl.lab02;

/**
 * Created by tkarl on 3/8/2018.
 */

public class list {
    int id;
    String date;
    String listName;

    public list(int id, String date, String listName) {
        this.id = id;
        this.date = date;
        this.listName = listName;
    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return date;
    }

    public String getMessage() {
        return listName;
    }
}
