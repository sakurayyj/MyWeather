package com.example.myweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.example.myweather.adapter.forecast_adapter;
import com.example.myweather.adapter.hour;
import com.example.myweather.adapter.hour_adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.amap.api.location.AMapLocationListener;
import com.example.myweather.database.MyDatabaseHelper;
import com.example.myweather.gson.LifeIndex;
import com.example.myweather.gson.LifeIndexOut;
import com.example.myweather.gson.WeatherDay;
import com.example.myweather.gson.WeatherDayOut;
import com.example.myweather.gson.Weather_24h;
import com.example.myweather.gson.Weather_24hOut;
import com.example.myweather.util.HttpUtil;
import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity<aMapLocation, aMapLocationListener> extends AppCompatActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefresh;

    private List<hour> list = new ArrayList<>();
    private List<hour> list1 = new ArrayList<>();

    private hour_adapter adapter;
    private forecast_adapter adapter1;
    private TextView now_temperature;
    private TextView highest_temperature;
    private TextView lowest_temperature;
    private TextView day_weather_conditions;
    private TextView air_quality;
    private TextView location;
    private ImageView search_location;
    private TextView clothes;
    private TextView cold;
    private TextView run;
    private TextView wash;
    private Button more_weather;
    private ImageView history_location;
    private ImageView orientation;

    private AMapLocationListener aMapLocationListener;
    private AMapLocation aMapLocation;

    private static String location_id = "101110102";
    private static String location_name = "长安区";
    private static String key = "a5cf6ab782a14013b08fb92a57dd2f72";
    private static String weather = "晴";
    private static String temp = "17";

    private MyDatabaseHelper databaseHelper;
    private SQLiteDatabase database ;
    private ContentValues values = new ContentValues();

    public static int flag = 0;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = getIntent();
        location = findViewById(R.id.location);
        search_location = findViewById(R.id.search_location);
        now_temperature = findViewById(R.id.now_temperature);
        highest_temperature = findViewById(R.id.highest_temperature);
        lowest_temperature = findViewById(R.id.lowest_temperature);
        day_weather_conditions = findViewById(R.id.day_weather_conditions);
        air_quality = findViewById(R.id.air_quality);
        wash = findViewById(R.id.wash);
        run = findViewById(R.id.run);
        clothes = findViewById(R.id.clothes);
        cold = findViewById(R.id.cold);
        orientation = findViewById(R.id.orientation);
        more_weather = findViewById(R.id.more_weather);
        history_location = findViewById(R.id.history_location);
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swip_refresh);
        swipeRefresh.setColorSchemeColors(R.color.black);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        location.setOnClickListener(this);
        more_weather.setOnClickListener(this);
        search_location.setOnClickListener(this);
        history_location.setOnClickListener(this);
        //location_id = intent.getStringExtra("location_id");
        //flag = intent.getIntExtra("flag",0);
        //location_name = intent.getStringExtra("location_name");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Log.d("LocationActivity","地点："+location_name);
        Log.d("LocationActivity","idid:："+location_id);
        databaseHelper = new MyDatabaseHelper(this,"CityStore.db",null,1);  //创建数据库
        databaseHelper.getWritableDatabase();
        database = databaseHelper.getWritableDatabase();
        gold();
        adapter = new hour_adapter(list);
        adapter1 = new forecast_adapter(list1);
        RecyclerView recyclerView =  findViewById(R.id.recycler_two);
        RecyclerView recyclerView1 =  findViewById(R.id.recycler_one);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        LinearLayoutManager manager1 = new LinearLayoutManager(MainActivity.this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView1.setLayoutManager(manager1);
        recyclerView1.setAdapter(adapter1);
    }

    private void refresh() {
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
                        gold();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void gold() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE
            },1);
        }else {
            getLocation();
        }
        aMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation.getErrorCode() == 0) {
                    Log.d("MainActivity","进来了");
                    if(flag == 0) {
                        location_id = aMapLocation.getLongitude() + "," + aMapLocation.getLatitude();
                        location_name = aMapLocation.getDistrict();
                        String sImage = "dingwei.png";
                        int image = MainActivity.this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
                        orientation.setImageResource(image);
                    }
                    Log.d("LocationActivity","最终id"+location_id);
                    location.setText(location_name);
                    String url_24H = "https://devapi.qweather.com/v7/weather/24h?location="+location_id+"&key="+key;
                    String url_7day = "https://devapi.qweather.com/v7/weather/7d?location="+location_id+"&key="+key;
                    String url_15day = "https://devapi.qweather.com/v7/weather/15d?location="+location_id+"&key="+key;
                    String url_life = "https://devapi.qweather.com/v7/indices/1d?type=1,2,3,9&location="+location_id+"&key="+key;
                    get_24h(url_24H);
                    Log.d("MainActivity",url_7day);
                    get_7day(url_7day);
                    get_index(url_life);
                    //Log.d("Get",now_temperature.getText().toString());


                }else {
                    Log.d("MainActivity","一开始出错");
                    if(flag == 0){
                        Toast.makeText(MainActivity.this, "获取地理位置错误", Toast.LENGTH_SHORT).show();
                        location_id = "101110102";
                        location_name = "长安区";
                    }
                    location.setText(location_name);
                    String url_24H = "https://devapi.qweather.com/v7/weather/24h?location="+location_id+"&key="+"a5cf6ab782a14013b08fb92a57dd2f72";
                    String url_7day = "https://devapi.qweather.com/v7/weather/7d?location="+location_id+"&key="+"a5cf6ab782a14013b08fb92a57dd2f72";
                    String url_15day = "https://devapi.qweather.com/v7/weather/15d?location="+location_id+"&key="+"a5cf6ab782a14013b08fb92a57dd2f72";
                    String url_life = "https://devapi.qweather.com/v7/indices/1d?type=1,2,3,9&location="+location_id+"&key="+"a5cf6ab782a14013b08fb92a57dd2f72";
                    get_24h(url_24H);
                    Log.d("MainActivity",url_7day);
                    get_7day(url_7day);
                    get_index(url_life);
                    if(location_id != null){
                        values.put("cityId",location_id);
                        values.put("cityName",location_name);
                        values.put("cityFlag",flag);
                        values.put("temperature",now_temperature.getText().toString());
                        values.put("weather",day_weather_conditions.getText().toString());
                        database.delete("City2","cityName = ?",new String[]{location_name});
                        database.insert("City2",null,values);
                        values.clear();
                    }
                }
            }
        } ;
    }

    private void get_index(String url_life) {
        HttpUtil.sendOkHttpRequest(url_life, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("Index","worry");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("Index","start");
                String responseData = null;
                Log.d("Index","run");
                try {
                    responseData = response.body().string();
                    Log.d("Index","res");
                } catch (IOException e) {
                    Log.d("Index","没response");
                    e.printStackTrace();
                }
                Gson gson = new Gson();
                Log.d("Index","gson");
                LifeIndexOut lifeIndexOut = gson.fromJson(responseData, LifeIndexOut.class);
                Log.d("Index","开始");
                runOnUiThread(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        LifeIndex[] lifeIndices = lifeIndexOut.getLife_arr();
                        Log.d("Index", String.valueOf(lifeIndices.length));
                        wash.setText(lifeIndices[0].getCategory());
                        run.setText(lifeIndices[1].getCategory());
                        clothes.setText(lifeIndices[2].getCategory());
                        cold.setText(lifeIndices[3].getCategory());
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case 1:
                if(grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if(grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "已经拒绝访问", Toast.LENGTH_SHORT).show();
                            finish();
                            return ;
                        }
                    }
                    getLocation();
                }
        }
    }



    public void getLocation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AMapLocationClient aMapLocationClient = new AMapLocationClient(getApplicationContext());
                AMapLocationClientOption option = new AMapLocationClientOption();
                option.setHttpTimeOut(20000);
                option.setInterval(10000);
                option.setOnceLocation(true);
                option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                aMapLocationClient.setLocationListener(aMapLocationListener);
                aMapLocationClient.setLocationOption(option);
                aMapLocationClient.stopLocation();
                aMapLocationClient.startLocation();
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_location:
            case R.id.location:
                Log.d("LocationActivity","开始");
                Intent intent = new Intent(MainActivity.this,LocationActivity.class);
                startActivityForResult(intent,1);
                Log.d("LocationActivity","结束");
                break;
            case R.id.more_weather:
                Intent intent2 = new Intent(MainActivity.this,moreActivity.class);
                intent2.putExtra("location_id",location_id);
                Log.d("moreActivity","开始传");
                startActivity(intent2);
                break;
            case R.id.history_location:
                Intent intent3 = new Intent(MainActivity.this,CityActivity.class);
                //intent3.putExtra("location_id",location_id);
                startActivityForResult(intent3,2);
                Log.d("CityActivity","结束2");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LocationActivity","code = "+requestCode);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    location_id = data.getStringExtra("location_id");
                    location_name = data.getStringExtra("location_name");
                    Log.d("LocationActivity","ld:"+location_id);
                    flag = 1;
                    gold();
                    //finish();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    location_id = data.getStringExtra("city_id");
                    flag = data.getIntExtra("flag",0);
                    location_name = data.getStringExtra("city_name");
                    Log.d("CityActivity","ld:"+location_id);
                    gold();
                    //flag = 1;
                    //finish();
                }
                break;
        }

    }

    private void get_7day(String url_7day) {
        Log.d("MainActivity","first");
        HttpUtil.sendOkHttpRequest(url_7day, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("worry","worry");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                list1.clear();
                Log.d("MainActivity","start");
                        String responseData = null;
                        Log.d("MainActivity","run");
                        try {
                            responseData = response.body().string();
                            Log.d("MainActivity","res");
                        } catch (IOException e) {
                            Log.d("MainActivity","没response");
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        Log.d("MainActivity","gson");
                        WeatherDayOut weatherDayOut = gson.fromJson(responseData, WeatherDayOut.class);
                        runOnUiThread(new Runnable() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void run() {
                                String weather, temp,id;
                                list.clear();
                                WeatherDay[] weatherDays = weatherDayOut.getWth_day_arr();
                                if(weatherDays[0].getTextDay().equals(weatherDays[0].getTextNight())) {
                                    weather =  weatherDays[0].getTextDay();
                                }else {
                                    weather = weatherDays[0].getTextDay() + "转" + weatherDays[1].getTextNight();
                                }
                                temp = weatherDays[0].getTempMax() + "° / " + weatherDays[0].getTempMin() + "°";

                                list1.add(new hour("今天",weather,temp,0));
                                if(weatherDays[1].getTextDay().equals(weatherDays[1].getTextNight())) {
                                    weather =  weatherDays[1].getTextDay();
                                }else {
                                    weather = weatherDays[1].getTextDay() + "转" + weatherDays[1].getTextNight();
                                }
                                temp = weatherDays[1].getTempMax() + "° / " + weatherDays[1].getTempMin() + "°";
                                list1.add(new hour("明天",weather,temp,0));
                                for(int i = 2;i < 7 ;i++){
                                    if(weatherDays[i].getTextDay().equals(weatherDays[i].getTextNight())) {
                                        weather = weatherDays[i].getTextDay();
                                    }else {
                                        weather = weatherDays[i].getTextDay() + "转" + weatherDays[i].getTextNight();
                                    }
                                    temp = weatherDays[i].getTempMax() + "° / " +weatherDays[i].getTempMin() + "°";
                                    list1.add(new hour(getWeek(getTime(i)),weather,temp,0));
                                }
                                highest_temperature.setText(weatherDays[0].getTempMax()+"℃");
                                lowest_temperature.setText(weatherDays[0].getTempMin()+"℃");
                                adapter1.notifyDataSetChanged();
                            }
                        });
            }
        });

    }



    private void get_24h(String url_24H) {
        HttpUtil.sendOkHttpRequest(url_24H, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "网络请求失败，请检查网络链接", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException{
                assert response.body() != null;
                String responseData = null;
                responseData = response.body().string();
                Gson gson = new Gson();
                Weather_24hOut weather24hOut = gson.fromJson(responseData, Weather_24hOut.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        int id;
                        Weather_24h[] weather_24hs = weather24hOut.getWth_hour_arr();
                        int flag = 1;
                        for (Weather_24h w:weather_24hs) {
                            id = getPhotoId(w.getWth_text());
                            list.add(new hour(w.getTime(),w.getWth_text(),w.getTemp(),id));
                            if(flag == 1){
                                temp = w.getTemp();
                                weather = w.getWth_text();
                                air_quality.setText(w.getWindDir());
                                now_temperature.setText(w.getTemp());
                                day_weather_conditions.setText(w.getWth_text());
                                if(location_id != null){
                                    values.put("cityId",location_id);
                                    values.put("cityName",location_name);
                                    values.put("cityFlag",flag);
                                    values.put("temperature",temp+"℃");
                                    values.put("weather",weather);
                                    database.delete("City2","cityName = ?",new String[]{location_name});
                                    database.insert("City2",null,values);
                                    values.clear();
                                }
                                flag = 0;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private int getPhotoId(String weather) {
        int id = 0;
        if(weather.equals("晴")){
            String sImage = "sunny.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("多云")){
            String sImage = "many_cloud.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("阴")){
            String sImage = "cloudy.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("小雨")){
            String sImage = "small_rain.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("中雨")){
            String sImage = "mid_rain.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("大雨")){
            String sImage = "big_rain.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        } else if(weather.equals("阵雨")){
            String sImage = "zhenyu.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("雷阵雨")){
            String sImage = "leizhenyu.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("xue") || weather.equals("xiaoxue")){
            String sImage = "leizhenyu.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("大雪")){
            String sImage = "daxue.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("雨夹雪")){
            String sImage = "yujiaxue.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }else if(weather.equals("中雪")){
            String sImage = "zhongxue.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }
        else{
            String sImage = "cloudy.png";
            id = this.getResources().getIdentifier(sImage.substring(0, sImage.length()-4), "drawable", getPackageName());
        }
        return id;
    }

    private Date getTime(int i){
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis()+24* 60 * 1000*60*i+8*60*1000*60);
        Log.d("time",format.format(date));
        return date;
    }

    private String getWeek(Date date) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 0 : calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDays[week];
    }

    public void init(List<hour> list) {
        for(int i = 1;i < 20; i++){
            String time = i+"时"+i+'分';
            String weather = "晴";
            String temperature = i+"℃";
            hour h = new hour(time,weather,temperature,0);
            list.add(h);
            //Log.d("MainActivity",list.get(i-1).getTime());
        }
    }


}