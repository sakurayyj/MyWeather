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
import com.example.myweather.adapter.hour_adapter;
import com.example.myweather.adapter.location;
import com.example.myweather.adapter.location_adapter;
import com.example.myweather.gson.Location;
import com.example.myweather.gson.LocationOut;
import com.example.myweather.gson.WeatherDay;
import com.example.myweather.gson.WeatherDayOut;
import com.example.myweather.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LocationActivity extends AppCompatActivity {

    private static String location_id = "101110102";
    private String location_name = "xian";
    private String key = "a5cf6ab782a14013b08fb92a57dd2f72";

    private String s;
    private EditText editText;
    private Button button;
    private Button back;

    private List<location> list1 = new ArrayList<>();

    private location_adapter adapter;

    public LocationActivity() {
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        editText = findViewById(R.id.exit_text);
        adapter = new location_adapter(LocationActivity.this,R.layout.recyclerview_three,list1);
        ListView listView = findViewById(R.id.recycler_three);
        listView.setAdapter(adapter);
        button = findViewById(R.id.search);
        back = findViewById(R.id.go_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = editText.getText().toString();
                if(s.equals("")){
                    Log.d("LocationActivity","地点"+s);
                }
                location_name = s;
                loadMessage();
            }
        });
       back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(LocationActivity.this,MainActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                location lc = list1.get(i);
                String ln = lc.getLocation();
                int j = ln.length()-1;
                for(j = ln.length()-1;j > 0; j-- ){
                    if(ln.charAt(j) == ' '){
                        break;
                    }
                }
                Intent intent = new Intent(LocationActivity.this,MainActivity.class);
                intent.putExtra("location_id",lc.getId());
                intent.putExtra("flag",1);
                intent.putExtra("location_name",ln.substring(j+1,ln.length()));
                Log.d("LocationActivity",lc.getLocation()+lc.getId());
                //startActivity(intent);
                setResult(RESULT_OK,intent);
                finish();
                //onDestroy();
            }
        });
    }

    private void loadMessage() {
        String url_location = "https://geoapi.qweather.com/v2/city/lookup?location=" + location_name + "&key=" + key;
        Log.d("LocationActivity", "location_name"+location_name);
        HttpUtil.sendOkHttpRequest(url_location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("worry", "worry");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("MainActivity", "start");
                        list1.clear();
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
                            LocationOut locationOut  = gson.fromJson(responseData, LocationOut.class);
                            runOnUiThread(new Runnable() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void run() {
                                    Location[] locations = locationOut.getLocation_arr();
                                    if(locations.length == 0){
                                        Log.d("LocationActivity","没存进去");
                                    }else{
                                        for (Location l:locations) {
                                            list1.add(new location(l.getAdm1()+" "+l.getAdm2()+" "+l.getName(),l.getName_id()));
                                            Log.d("LocationActivity",l.getName());
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {

                                              @Override
                                              public void run() {
                                                  Toast.makeText(LocationActivity.this, "地点输入错误", Toast.LENGTH_SHORT).show();
                                                  editText.setText("");
                                              }
                            });


                        }

            }
        });
    }
}