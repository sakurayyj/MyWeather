package com.example.myweather.adapter;

public class city {

    private String id;

    private String name;

    private String temp;

    private String weather;

    private int flag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public city(String name, String temp, String weather, String id, int flag){
        this.name = name;
        this.weather = weather;
        this.temp = temp;
        this.id = id;
        this.flag = flag;
    }
}
