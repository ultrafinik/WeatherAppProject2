package com.example.weatherappproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.weatherappproject.api.NetworkService;
import com.example.weatherappproject.model.MainWeather;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    public DetailViewModel(@NonNull Application application) {
        super(application);

        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData();
    }

    public void logOut() {
        authAppRepository.logOut();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
    private MutableLiveData<MainWeather> weather;
    private void LoadData(String city)
    {
        NetworkService.getInstance().getJSONApi().getWeatherByCity(city,NetworkService.KEY,"metric","ru").enqueue(new Callback<MainWeather>() {
            @Override
            public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                MainWeather weatherNow=response.body();
                weather.setValue(weatherNow);
            }

            @Override
            public void onFailure(Call<MainWeather> call, Throwable t) {
                Log.d("error",t.getMessage());
            }
        });
    }
    public MutableLiveData<MainWeather> getWeather(String city){
        if(weather==null){
            weather=new MutableLiveData<>();
            LoadData(city);
        }
        return weather;
    }
}