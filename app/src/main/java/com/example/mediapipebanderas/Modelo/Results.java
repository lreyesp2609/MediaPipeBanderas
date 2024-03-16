package com.example.mediapipebanderas.Modelo;

import com.google.gson.annotations.SerializedName;

public class Results {

    @SerializedName("Name")
    private String name;

    @SerializedName("Capital")
    private Capital capital;

    @SerializedName("CountryCodes")
    private CountryCodes countryCodes;

    @SerializedName("GeoRectangle")
    private GeoRectangle geoRectangle;

    @SerializedName("TelPref")
    String telPref;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Capital getCapital() {
        return capital;
    }

    public void setCapital(Capital capital) {
        this.capital = capital;
    }

    public CountryCodes getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(CountryCodes countryCodes) {
        this.countryCodes = countryCodes;
    }

    public GeoRectangle getGeoRectangle() {
        return geoRectangle;
    }

    public void setGeoRectangle(GeoRectangle geoRectangle) {
        this.geoRectangle = geoRectangle;
    }

    public String getTelPref() {
        return telPref;
    }

    public void setTelPref(String telPref) {
        this.telPref = telPref;
    }
}
