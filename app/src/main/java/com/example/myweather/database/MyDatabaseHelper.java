package com.example.myweather.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    public static final String CREATE_City = "create table City2 ("
            + "cityFlag Integer,"
            + "cityId text ,"
            + "cityName text ,"
            + "temperature text,"
            + "weather text)";

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL(CREATE_BOOK);
        //sqLiteDatabase.execSQL(CREATE_CATEGORY);
        sqLiteDatabase.execSQL(CREATE_City);
        Log.d("Database","创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("drop table if exists City");
//        sqLiteDatabase.execSQL("drop table if exists Category");
//        onCreate(sqLiteDatabase);
    }
}
