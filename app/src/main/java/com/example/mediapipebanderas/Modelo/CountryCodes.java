package com.example.mediapipebanderas.Modelo;

import com.google.gson.annotations.SerializedName;

public class CountryCodes {

    @SerializedName("iso2")
    private String iso2;

    @SerializedName("isoN")
    private int isoN;

    @SerializedName("iso3")
    private String iso3;

    @SerializedName("fips")
    private String fips;

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public int getIsoN() {
        return isoN;
    }

    public void setIsoN(int isoN) {
        this.isoN = isoN;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getFips() {
        return fips;
    }

    public void setFips(String fips) {
        this.fips = fips;
    }


}
