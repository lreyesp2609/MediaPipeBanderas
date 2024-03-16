package com.example.mediapipebanderas.Modelo;

import com.google.gson.annotations.SerializedName;

public class CountryResponse {

    @SerializedName("StatusMsg")
    private String statusMsg;

    @SerializedName("Results")
    private Results results;

    @SerializedName("StatusCode")
    private int statusCode;

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Results getResults() {
        return results;
    }

    public void setResults(Results results) {
        this.results = results;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

