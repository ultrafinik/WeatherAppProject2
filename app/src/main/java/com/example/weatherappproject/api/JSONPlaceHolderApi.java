package com.example.weatherappproject.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JSONPlaceHolderApi {
    @GET("/posts/{id}")
    public Call<POST> getPostWithID(@Query("id") String id, @Query("appid") String appid,@Query("units") String units,@Query("lang") String lang);
}

