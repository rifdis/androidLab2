package com.example.tkarl.lab02;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Ref;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ListView extends BaseActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
    private static DBManager dbManager;
    private static SQLiteDatabase database;
    private static ArrayList<String> itemNameList;
    private static ArrayList<Item> itemList;
    private static Item item;
    private static int getid;
    private static Item thisItem;


    final String ITEM = "ITEM";
    final String ITEMDATE = "DATE";
    final String STATUS = "STATUS";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String name = "";

            itemList = new ArrayList<Item>();
            itemNameList = new ArrayList<String>();
        dbManager = new DBManager(this);
        getid = getIntent().getIntExtra("ListID",0);

        if (getid == 0){
            Toast.makeText(this, "No list item selected", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "You have selected a list item " +  getid, Toast.LENGTH_SHORT).show();
        TextView header = (TextView)findViewById(R.id.listnameinview);
       database = dbManager.getReadableDatabase();

        Cursor ListName = database.rawQuery("SELECT " + dbManager.L_ListName + " FROM " + dbManager.L_TABLE + " WHERE " + dbManager.L_ID + " = " + getid,null);
        while (ListName.moveToNext()){
            name =   ListName.getString(ListName.getColumnIndex(DBManager.L_ListName));
        }

      header.setText(name);
        Spinner listSpinner = (Spinner) findViewById(R.id.item_view_Item_Spinner);
        listSpinner.setOnItemSelectedListener(this);

        Button addItem = (Button)findViewById(R.id.add_item_button);
        addItem.setOnClickListener(this);
        RefreshItems();


    }

   @Override
    public void onClick(View v) {
        EditText editText = (EditText)findViewById(R.id.New_Item_add);
        String editString = editText.getText().toString();
        database = dbManager.getWritableDatabase();
        if (editString.length() < 1 || editString.trim().length() == 0){
            Toast.makeText(this, "Please Enter a name for the list", Toast.LENGTH_SHORT).show();
        }
        if(itemNameList.contains(editString)){
            Toast.makeText(this, editString + " Already exists in the list", Toast.LENGTH_SHORT).show();
        }
        else {
            DateFormat df = new SimpleDateFormat("MM/DD/YY");
            Date date = new Date();
            date.getDate();
            String TodayDate = df.format(date);
            Toast.makeText(this, TodayDate, Toast.LENGTH_SHORT).show();

            try {


                database.execSQL("INSERT INTO " + dbManager.I_TABLE+ " ( "+ DBManager.I_ITEM  + " , " + DBManager.I_DATE+" , " +  dbManager.I_LIST+ " ) VALUES (  '" + editString + "' , '" + TodayDate + "', '"+ getid +"' )");
                itemNameList.add(editString);


                Toast.makeText(this, editString +" created", Toast.LENGTH_LONG).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "Issue inserting List" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }


    }

   private void RefreshItems(){

        int ItemID = 0;
        String ItemName = "";
        String ItemDate = "";
        int ItemStatus = 0;

        Cursor Items= database.rawQuery("SELECT "+ dbManager.I_ID+ " ," + dbManager.I_ITEM + " , " + dbManager.I_DATE+ ", "+dbManager.I_COMPLETE+ " FROM " +dbManager.I_TABLE + " WHERE " + dbManager.I_LIST + " = " + getid + " ORDER BY " + dbManager.I_ID,null);
        while (Items.moveToNext()){
            ItemID = Items.getInt(Items.getColumnIndex(dbManager.I_ID));
            ItemName = Items.getString(Items.getColumnIndex(dbManager.I_ITEM));
            ItemDate = Items.getString(Items.getColumnIndex(dbManager.I_DATE));
            ItemStatus = Items.getInt(Items.getColumnIndex(dbManager.I_COMPLETE));
            thisItem = new Item(ItemID, ItemDate,ItemName,ItemStatus);
           itemList.add(thisItem);
            itemNameList.add(thisItem.getMessage() + "  Created on" + thisItem.getDate());
            Toast.makeText(this, thisItem.getMessage() +" Found", Toast.LENGTH_SHORT).show();
        }
     populateSpinner(itemNameList);

    }
    public void populateSpinner(ArrayList items){



        Spinner listSpinner = (Spinner) findViewById(R.id.item_view_Item_Spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_row_item_layout, items);

        adapter.setDropDownViewResource(R.layout.spinner_row_item_layout);

        listSpinner.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
