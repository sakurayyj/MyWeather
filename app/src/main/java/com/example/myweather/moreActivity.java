package com.example.myweather;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myweather.adapter.forecast_adapter;
import com.example.myweather.adapter.hour;
import com.example.myweather.adapter.location;
import com.example.myweather.adapter.location_adapter;
import com.example.myweather.gson.Location;
import com.example.myweather.gson.LocationOut;
import com.example.myweather.gson.WeatherDay;
import com.example.myweather.gson.WeatherDayOut;
import com.example.myweather.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class moreActivity extends AppCompatActivity {

    private static String location_id = "101110102";
    private String location_name = "xian";
    private String key = "a5cf6ab782a14013b08fb92a57dd2f72";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    private String s;
    private Button back;

    private List<hour> list = new ArrayList<>();

    private forecast_adapter adapter;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Intent intent = getIntent();
        location_id = intent.getStringExtra("location_id");
        Log.d("moreActivity","传输成功");
        back = findViewById(R.id.back2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(LocationActivity.this,MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        adapter = new forecast_adapter(list);
        loadMessage();
        LinearLayoutManager manager = new LinearLayoutManager(moreActivity.this);
        RecyclerView recyclerView =  findViewById(R.id.more_weather);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void loadMessage() {
        String url_location = "https://devapi.qweather.com/v7/weather/15d?location=" + location_id + "&key=" + key;
        Log.d("LocationActivity", "location_id:"+location_id);
        HttpUtil.sendOkHttpRequest(url_location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("worry", "worry");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("MainActivity", "start");
                String responseData = null;
                Log.d("MainActivity", "run");
                try {
                    responseData = response.body().string();
                    Log.d("MainActivity", "res");
                } catch (IOException e) {
                    Log.d("MainActivity", "没response");
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Log.d("MainActivity", "gson");
                try{

                }catch(Exception e){
                    e.printStackTrace();

                }
                WeatherDayOut weatherDayOut = gson.fromJson(responseData, WeatherDayOut.class);
                runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        String weather, temp, id;
                        WeatherDay[] weatherDays = weatherDayOut.getWth_day_arr();
                        Log.d("MainActivity", "OK");
                        if (weatherDays[0].getTextDay().equals(weatherDays[0].getTextNight())) {
                            weather = weatherDays[0].getTextDay();
                        } else {
                            weather = weatherDays[0].getTextDay() + "转" + weatherDays[1].getTextNight();
                        }
                        temp = weatherDays[0].getTempMax() + "° / " + weatherDays[0].getTempMin() + "°";

                        list.add(new hour("今天", weather, temp, 0));
                        if (weatherDays[1].getTextDay().equals(weatherDays[1].getTextNight())) {
                            weather = weatherDays[1].getTextDay();
                        } else {
                            weather = weatherDays[1].getTextDay() + "转" + weatherDays[1].getTextNight();
                        }
                        temp = weatherDays[1].getTempMax() + "° / " + weatherDays[1].getTempMin() + "°";
                        list.add(new hour("明天", weather, temp, 0));
                        for (int i = 2; i < 15; i++) {
                            if (weatherDays[i].getTextDay().equals(weatherDays[i].getTextNight())) {
                                weather = weatherDays[i].getTextDay();
                            } else {
                                weather = weatherDays[i].getTextDay() + "转" + weatherDays[i].getTextNight();
                            }
                            temp = weatherDays[i].getTempMax() + "° / " + weatherDays[i].getTempMin() + "°";
                            list.add(new hour(format.format(getTime(i)).substring(0,11)+getWeek(getTime(i)), weather, temp, 0));
                        }
                        Log.d("moreActivity",getTime(1).toString());
                        adapter.notifyDataSetChanged();
                    }
                });

            }

            private Date getTime(int i) {
                Date date = new Date(System.currentTimeMillis() + 24 * 60 * 1000 * 60 * i + 8 * 60 * 1000 * 60);
                return date;
            }

            private String getWeek(Date date) {
                String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int week = calendar.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 0 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
                return weekDays[week];
            }

        });
    }
}