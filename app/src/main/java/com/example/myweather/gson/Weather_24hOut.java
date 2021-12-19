package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Weather_24hOut {
    @SerializedName("code")
    private String code;

    @SerializedName("hourly")
    private Weather_24h[] weather_24hs;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Weather_24h[] getWth_hour_arr() {
        return weather_24hs;
    }

    public void setWth_hour_arr(Weather_24h[] weather_24hs) {
        this.weather_24hs = weather_24hs;
    }
}

