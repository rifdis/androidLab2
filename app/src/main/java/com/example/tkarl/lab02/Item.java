package com.example.tkarl.lab02;

/**
 * Created by tkarl on 3/14/2018.
 */
public class Item {
    int id;
    String date;
    String itemMessage;
    int completed;


    public Item(int item_id, String item_date, String itemMessage,int completed) {
        this.id = item_id;
        this.date = item_date;
        this.itemMessage = itemMessage;
        this.completed = completed;

    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }


    public int getStatus() {
        return completed;
    }
    public String getMessage() {
        return itemMessage;
    }

}