package com.example.adiapp.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.adiapp.Models.Category;
import com.example.adiapp.Models.Question;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DBAdidas extends SQLiteAssetHelper {

    private static final String DB_NAME = "AdidasT.db";
    private static final int DB_VER =1 ;

    private static DBAdidas adidasDB;



    public static synchronized DBAdidas getInstance(Context context){

        if (adidasDB==null) adidasDB=new DBAdidas(context);
        return adidasDB;



    }

    public DBAdidas(Context context) {

        super(context, DB_NAME,  null ,DB_VER);
    }


    //Получение всех Категорий из БД

    public List<Category> getAllCategories(){
                SQLiteDatabase db = adidasDB.getWritableDatabase();

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





    //Получение вопросов из БД
    public List<Question> getQuestionByCategory(int category){
        SQLiteDatabase db = adidasDB.getWritableDatabase();

        Cursor cursor = db.rawQuery(String.format("SELECT * FROM Question WHERE CategoryID = %d ORDER BY RANDOM() LIMIT 30",category), null);
        List<Question> questions= new ArrayList<>();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
               Question question=new Question(cursor.getInt(cursor.getColumnIndex("ID")),
                       cursor.getString(cursor.getColumnIndex("QuestionText")),
                       cursor.getString(cursor.getColumnIndex("QuestionImage")),
                       cursor.getString(cursor.getColumnIndex("AnswerA")),
                       cursor.getString(cursor.getColumnIndex("AnswerB")),
                       cursor.getString(cursor.getColumnIndex("AnswerC")),
                       cursor.getString(cursor.getColumnIndex("AnswerD")),
                       cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                       cursor.getInt(cursor.getColumnIndex("IsImageQuestion")),
                       cursor.getInt(cursor.getColumnIndex("CategoryID")));
                questions.add(question);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();

        return questions;



    }

}
