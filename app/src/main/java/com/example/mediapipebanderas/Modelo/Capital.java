package com.example.mediapipebanderas.Modelo;

import com.google.gson.annotations.SerializedName;

public class Capital {

    @SerializedName("Name")
    private String name;

    @SerializedName("GeoPt")
    private double[] geoPt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getGeoPt() {
        return geoPt;
    }

    public void setGeoPt(double[] geoPt) {
        this.geoPt = geoPt;
    }
}
