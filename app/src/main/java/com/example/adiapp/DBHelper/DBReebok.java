package com.example.adiapp.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.adiapp.Models.Category;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DBReebok extends SQLiteAssetHelper {
   private static  String DB_NAME="ADIApp2020.db";
    private static final int DB_VER=1;

   private static DBReebok dbReebok;




    public static synchronized DBReebok getInstance(Context context){

        if (dbReebok==null) dbReebok=new DBReebok(context);
        return dbReebok;

    }
    public DBReebok(Context context) {
        super(context,DB_NAME,null,DB_VER);


    }
    public List<Category> getAllCategories(){
        SQLiteDatabase db = dbReebok.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Category ;", null);
        List<Category> categories = new ArrayList<>();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                Category category = new Category(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("Image")));
                categories.add(category);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return categories;

    }
}
