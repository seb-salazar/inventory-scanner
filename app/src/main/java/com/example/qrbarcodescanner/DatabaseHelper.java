package com.example.qrbarcodescanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String COLUMN_ITEM_NAME = "ITEM_NAME";
    public static final String COLUMN_BRAND_NAME = "BRAND_NAME";
    public static final String COLUMN_BARCODE_NUMBER = "BARCODE_NUMBER";
    public static final String COLUMN_ITEM_CATEGORY = "ITEM_CATEGORY";
    public static final String COLUMN_ITEM_AMOUNT = "ITEM_AMOUNT";
    public static final String COLUMN_ITEM_DATE = "ITEM_DATE";
    public static final String COLUMN_ID = "ID";


    public DatabaseHelper(@Nullable Context context) {
        super(context, "item.db", null, 1);
        //you change the db version
    }

    //called the first time a database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + ITEM_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ITEM_NAME + " TEXT, " + COLUMN_BRAND_NAME + " TEXT, " + COLUMN_BARCODE_NUMBER + " TEXT, " + COLUMN_ITEM_CATEGORY + " TEXT, " + COLUMN_ITEM_AMOUNT + " TEXT, " + COLUMN_ITEM_DATE + " TEXT)";
        db.execSQL(createTableStatement);
    }

    //called if the database version number changes.
    //prevents breaking when you change the design
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(ItemModel itemModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues(); //equivalente to hasmap or asociative array or intent (put extras)

        cv.put(COLUMN_ITEM_NAME, itemModel.getName());
        cv.put(COLUMN_BRAND_NAME, itemModel.getBrand());
        cv.put(COLUMN_BARCODE_NUMBER, itemModel.getNumber());
        cv.put(COLUMN_ITEM_CATEGORY, itemModel.getCategory());
        cv.put(COLUMN_ITEM_AMOUNT, itemModel.getAmount());
        cv.put(COLUMN_ITEM_DATE, itemModel.getDate());
        //id is not necessary because is an auto increment



        long insert = db.insert(ITEM_TABLE, null, cv);
        //check documentation for empty column in null column hack

        //long makes that if it is -1 means a fail (is a succesful variable)

        if (insert == -1) {
            return false;

        }
        else {
            return true;
        }

    }


    public boolean deleteItem(ItemModel itemModel){
        //find item in db. If found, delete and return true
        //if not, return false

        //DAO style (Data Access Object)
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + ITEM_TABLE + " WHERE " + COLUMN_ID + " = " + itemModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }



    public boolean searchForItem(String string){
        //find item in db. If found, delete and return true
        //if not, return false

        //DAO style (Data Access Object)
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "SELECT * FROM " + ITEM_TABLE + " WHERE " + COLUMN_BARCODE_NUMBER + " = " + string;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean updateAmountUP(String amount, String barcodeNumber){
        //find item in db. If found, delete and return true
        //if not, return false

        String[] bindingArgs = new String[]{ amount, barcodeNumber };
        //DAO style (Data Access Object)
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE + " SET " + COLUMN_ITEM_AMOUNT + " = " + COLUMN_ITEM_AMOUNT + " + ?" + " WHERE " + COLUMN_BARCODE_NUMBER + " = ?", bindingArgs);

        db.close();

        return true;
    }

    public boolean updateAmountDOWN(String amount, String barcodeNumber){
        //find item in db. If found, delete and return true
        //if not, return false

        String[] bindingArgs = new String[]{ amount, barcodeNumber };
        //DAO style (Data Access Object)
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE + " SET " + COLUMN_ITEM_AMOUNT + " = " + COLUMN_ITEM_AMOUNT + " - ?" + " WHERE " + COLUMN_BARCODE_NUMBER + " = ?", bindingArgs);

        //to make negative numbers equal to 0
        db.execSQL("UPDATE " + ITEM_TABLE + " SET " + COLUMN_ITEM_AMOUNT + " = " + " 0 " + " WHERE " + COLUMN_ITEM_AMOUNT + " < 0");

        db.close();

        return true;
    }




    public boolean updateDate(String date, String barcodeNumber){
        //find item in db. If found, delete and return true
        //if not, return false

        String[] bindingArgs = new String[]{ date, barcodeNumber };
        //DAO style (Data Access Object)
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + ITEM_TABLE + " SET " + COLUMN_ITEM_DATE + " = ?" + " WHERE " + COLUMN_BARCODE_NUMBER + " = ?", bindingArgs);

        db.close();

        return true;
    }



    //To get the data from the db, as a list
    public List<String[]> getAllItems() {

        List<String[]> returnList = new ArrayList<String[]>();

        //Get the data from database

        String queryString = "SELECT * FROM " + ITEM_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        //use getWritableDatabase only when you plan to insert, update and delete records
        //use getReadableDatabase when you plan to ONLY SELECT items from database (it locks the db)

        Cursor cursor = db.rawQuery(queryString, null);
        //search for the selection args option

        //a cursor is the result from a query (an array of items). a result set
        //remember the light bulb
        if (cursor.moveToFirst()) {
            //move to first is the first value, shows that there is a new line, at the beginnig to read
            //loop through the cursor (result set) and create new customer objects. Put
            //them in the return list

            do {
                int itemID = cursor.getInt(0);
                //to put index 0 you must know how the table looks like
                //otherwise it will crash
                String itemName = cursor.getString(1);
                String itemBrand = cursor.getString(2);
                String itemBarCode = cursor.getString(3);
                String itemCategory = cursor.getString(4);
                String itemAmount = cursor.getString(5);
                String itemDate = cursor.getString(6);
                //if it were a boolean you would use
                //boolean x = cursor.getInt(y) == 1 ? true : false;
                //ternary operator

                //ItemModel newItem = new ItemModel(itemID, itemName, itemBrand, itemBarCode, itemCategory, itemAmount, itemDate);
                //change String to Item model to get the original


                String barCode = itemBarCode;
                String name = itemName;
                String amount = itemAmount;

                String[] stringArray = new String[]{name, barCode, amount};

                returnList.add(stringArray);



            }while (cursor.moveToNext());  //while there are lines to read

        }
        else {
            //failure. it doesnt add anything to the list

        }

        //you have to clean after yourself
        //close both the cursor and the db when done.
        cursor.close();
        db.close();

        return returnList;

    }




}

