package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Weather_24h {
    @SerializedName("fxTime")
    private String time;

    @SerializedName("temp")
    private String temperature;

    @SerializedName("text")
    private String wth_text;

    public String getWth_text() {
        return wth_text;
    }

    public void setWth_text(String wth_text) {
        this.wth_text = wth_text;
    }

    @SerializedName("windScale")
    private String windScale;

    @SerializedName("windDir")
    private String windDir;



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temperature;
    }

    public void setTemp(String temperature) {
        this.temperature = temperature;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }
}

