package com.example.mediapipebanderas.Interface;

import com.example.mediapipebanderas.Modelo.CountryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CountryApiService {

    @GET("countries/info/{alpha2code}.json")
    Call<CountryResponse> getCountryInfo(@Path("alpha2code") String alpha2code);
}
