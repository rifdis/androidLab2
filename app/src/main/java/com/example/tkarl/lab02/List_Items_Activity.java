package com.example.tkarl.lab02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class List_Items_Activity extends BaseActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    private static ArrayList<String> ListItems;

    private static int ArrayIndex;
    private static DBManager dbManager;
   private static SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListItems = new ArrayList<>();

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_list__items_);

        dbManager = new DBManager(this);
        Button button = (Button)findViewById(R.id.new_list_item_add_button);
        button.setOnClickListener(this);


        populateSpinner(ListItems);
        Spinner listSpinner = (Spinner) findViewById(R.id.List_Item_Spinner);
        listSpinner.setOnItemSelectedListener(this);


        refreshList();
    }

    public void refreshList(){
        Spinner listSpinner = (Spinner) findViewById(R.id.List_Item_Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_row_item_layout, ListItems);

        adapter.clear();
        listSpinner.setAdapter(null);



        database =dbManager.getReadableDatabase();
        Cursor cursor = database.query(DBManager.L_TABLE,null,null,null,null,null,DBManager.L_ID);
        String date, name;
        int id;
        while (cursor.moveToNext()){
            id = cursor.getInt(cursor.getColumnIndex(DBManager.L_ID));
            date = cursor.getString(cursor.getColumnIndex(DBManager.L_DATE));
            name = cursor.getString(cursor.getColumnIndex(DBManager.L_ListName));

           String seeID = Integer.toString(id);

            ListItems.add(name);

        }
        ListItems.add(0,getString(R.string.Selectalist));
        if(ListItems.size() == 1) {

            ListItems.set(0,getString(R.string.noItemAdded));
        }


        populateSpinner(ListItems);
       }
    public void populateSpinner(ArrayList items){



        Spinner listSpinner = (Spinner) findViewById(R.id.List_Item_Spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_row_item_layout, items);

            adapter.setDropDownViewResource(R.layout.spinner_row_item_layout);

            listSpinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner listSpinner = (Spinner) findViewById(R.id.List_Item_Spinner);
        String selValue = listSpinner.getSelectedItem().toString();
        database = dbManager.getReadableDatabase();

        if(position != 0) {
            int getid = 0;
            Cursor getChosenID = database.rawQuery("SELECT " + dbManager.L_ID + " FROM " + dbManager.L_TABLE + " WHERE " + dbManager.L_ListName + " = " + "'" + selValue + "'",null);
            while (getChosenID.moveToNext()){
                getid =   getChosenID.getInt(getChosenID.getColumnIndex(DBManager.L_ID));
            }


            Toast.makeText(this, "You have selected a list item " +  getid, Toast.LENGTH_SHORT).show();
            Intent seeListItemsIntent = new Intent(this,ListView.class);

            seeListItemsIntent.putExtra("ListID",getid);

            startActivity(seeListItemsIntent);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        EditText editText = (EditText)findViewById(R.id.Add_List_Text_Field);
        String editString = editText.getText().toString();
        database = dbManager.getWritableDatabase();
        if (editString.length() < 1 || editString.trim().length() == 0){
            Toast.makeText(this, "Please Enter a name for the list", Toast.LENGTH_SHORT).show();
        }
        if(ListItems.contains(editString)){
            Toast.makeText(this, editString + " Already exists in the list", Toast.LENGTH_SHORT).show();
        }
        else {
            DateFormat df = new SimpleDateFormat("MM/DD/YY");
            Date date = new Date();
            date.getDate();
            String TodayDate = df.format(date);
            Toast.makeText(this, TodayDate, Toast.LENGTH_SHORT).show();

           try {


               database.execSQL("INSERT INTO " + dbManager.L_TABLE + " ( "+ DBManager.L_DATE  + " , " + DBManager.L_ListName +  " ) VALUES (  '" + TodayDate + "' , '" + editString + "' )");
               //ListItems.add(editString);
              // populateSpinner(ListItems);
               refreshList();
               Toast.makeText(this, editString +" created", Toast.LENGTH_LONG).show();
           }
           catch (Exception e) {
               Toast.makeText(this, "Issue inserting List" + e.getMessage(), Toast.LENGTH_LONG).show();
           }
           }

        }
    }

