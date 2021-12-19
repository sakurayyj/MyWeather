package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class WeatherDayOut {
    @SerializedName("code")
    private String code;

    @SerializedName("daily")
    private WeatherDay[] weatherDays;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public WeatherDay[] getWth_day_arr() {
        return weatherDays;
    }

    public void setWth_day_arr(WeatherDay[] weatherDays) {
        this.weatherDays = weatherDays;
    }
}
