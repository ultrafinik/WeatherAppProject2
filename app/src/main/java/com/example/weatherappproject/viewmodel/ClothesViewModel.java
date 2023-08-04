package com.example.weatherappproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherappproject.api.NetworkService;
import com.example.weatherappproject.model.MainWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClothesViewModel extends AndroidViewModel {
    private MutableLiveData<MainWeather> weather;
    public ClothesViewModel(@NonNull Application application) {
        super(application);
        NetworkService.getInstance().getJSONApi().getPostWithID("Калининград",NetworkService.KEY,"metric","ru").enqueue(new Callback<MainWeather>() {
            @Override
            public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                MainWeather weatherNow=response.body();
                weather.setValue(weatherNow);
            }

            @Override
            public void onFailure(Call<MainWeather> call, Throwable t) {

            }
        });
    }
    public MutableLiveData<MainWeather> getWeather(){
       return weather;
    }
}
