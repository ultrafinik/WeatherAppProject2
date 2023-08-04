package com.example.weatherappproject.api;

import com.example.weatherappproject.model.MainWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    @GET("weather")
    public Call<MainWeather> getPostWithID(@Query("q") String q, @Query("appid") String appid, @Query("units") String units, @Query("lang") String lang);
}

