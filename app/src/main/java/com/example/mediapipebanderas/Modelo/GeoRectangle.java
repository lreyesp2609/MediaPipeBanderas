package com.example.mediapipebanderas.Modelo;

import com.google.gson.annotations.SerializedName;

public class GeoRectangle {

    @SerializedName("West")
    private double west;

    @SerializedName("East")
    private double east;

    @SerializedName("North")
    private double north;

    @SerializedName("South")
    private double south;

    public double getWest() {
        return west;
    }

    public void setWest(double west) {
        this.west = west;
    }

    public double getEast() {
        return east;
    }

    public void setEast(double east) {
        this.east = east;
    }

    public double getNorth() {
        return north;
    }

    public void setNorth(double north) {
        this.north = north;
    }

    public double getSouth() {
        return south;
    }

    public void setSouth(double south) {
        this.south = south;
    }
}
