package com.example.myweather.gson;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String name_id;

    @SerializedName("adm2")
    private String adm2;

    @SerializedName("adm1")
    private String adm1;

    @SerializedName("country")
    private String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_id() {
        return name_id;
    }

    public void setName_id(String name_id) {
        this.name_id = name_id;
    }

    public String getAdm2() {
        return adm2;
    }

    public void setAdm2(String adm2) {
        this.adm2 = adm2;
    }

    public String getAdm1() {
        return adm1;
    }

    public void setAdm1(String adm1) {
        this.adm1 = adm1;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

