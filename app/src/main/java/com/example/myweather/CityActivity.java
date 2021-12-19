package com.example.myweather;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myweather.adapter.city;
import com.example.myweather.adapter.city_adapter;
import com.example.myweather.adapter.hour;
import com.example.myweather.database.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CityActivity extends AppCompatActivity {

    private List<city> list = new ArrayList<>();

    private city_adapter adapter;

    public static MyDatabaseHelper helper;

    private Button city_back;

    //private SwipeRefreshLayout swipeRefresh;

    public static String C_city_name;
    public static int C_flag;
    public static String C_city_id;
    public static String C_temperature;
    public static String C_weather;
    public  Button cityBack;
    public RecyclerView recyclerView;

    private city_adapter.OnItemClickListener mOnItemClickListener;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swip_refresh);
//        swipeRefresh.setColorSchemeColors(R.color.black);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refresh();
//            }
//        });
        city_back = findViewById(R.id.city_back);
        city_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        helper = new MyDatabaseHelper(this,"CityStore.db",null,1);
        recyclerView = findViewById(R.id.recycler_four);
        LinearLayoutManager manager = new LinearLayoutManager(CityActivity.this);
        recyclerView.setLayoutManager(manager);
        init();
//        adapter.setOnItemClickListener(new city_adapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
//        recyclerView = findViewById(R.id.recycler_four);
//        LinearLayoutManager manager = new LinearLayoutManager(CityActivity.this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(manager);
    }

    @SuppressLint("Range")
    private void init() {
        SQLiteDatabase database = helper.getWritableDatabase();
        Cursor cursor = database.query("City2",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                C_flag = cursor.getInt(cursor.getColumnIndex("cityFlag"));
                C_city_name = cursor.getString(cursor.getColumnIndex("cityName"));
                C_city_id = cursor.getString(cursor.getColumnIndex("cityId"));
                C_temperature = cursor.getString(cursor.getColumnIndex("temperature"));
                C_weather = cursor.getString(cursor.getColumnIndex("weather"));
                city c = new city(C_city_name,C_temperature,C_weather,C_city_id,C_flag);
                list.add(c);
            }while(cursor.moveToNext());
        }
        adapter = new city_adapter(list,CityActivity.this);
        recyclerView.setAdapter(adapter);
        cursor.close();
    }

    public void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                        //swipeRefresh.setRefreshing(false);

                    }
                });
            }
        }).start();
    }
}