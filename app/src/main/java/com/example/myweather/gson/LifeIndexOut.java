package com.example.myweather.gson;


import com.google.gson.annotations.SerializedName;

public class LifeIndexOut {
    @SerializedName("code")
    private String code;

    @SerializedName("daily")
    private LifeIndex[] lifeIndices;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LifeIndex[] getLife_arr() {
        return lifeIndices;
    }

    public void setWth_hour_arr(LifeIndex[] lifeIndices) {
        this.lifeIndices = lifeIndices;
    }
}
