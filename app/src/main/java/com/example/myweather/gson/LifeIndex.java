package com.example.myweather.gson;


import com.google.gson.annotations.SerializedName;

public class LifeIndex {

    @SerializedName("type")
    private String type;

    @SerializedName("category")
    private String category;

    @SerializedName("text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
