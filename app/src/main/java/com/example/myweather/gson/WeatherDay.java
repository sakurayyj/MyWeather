package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class WeatherDay {
    @SerializedName("fxDate")
    private String time;

    @SerializedName("tempMax")
    private String tempMax;

    @SerializedName("tempMin")
    private String tempMin;

    @SerializedName("textDay")
    private String textDay;

    @SerializedName("textNight")
    private String textNight;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTextDay() {
        return textDay;
    }

    public void setTextDay(String textDay) {
        this.textDay = textDay;
    }

    public String getTextNight() {
        return textNight;
    }

    public void setTextNight(String textNight) {
        this.textNight = textNight;
    }
}
