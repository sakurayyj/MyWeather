package com.example.myweather.gson;


import com.google.gson.annotations.SerializedName;

public class LocationOut {

    @SerializedName("code")
    private String code;

    @SerializedName("location")
    private Location[] location_arr;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Location[] getLocation_arr() {
        return location_arr;
    }

    public void setLocation_arr(Location[] location_arr) {
        this.location_arr = location_arr;
    }
}
