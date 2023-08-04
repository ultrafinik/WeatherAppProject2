package com.example.weatherappproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weatherappproject.R;
import com.example.weatherappproject.model.MainWeather;
import com.example.weatherappproject.viewmodel.ClothesViewModel;

import org.w3c.dom.Text;

import java.time.LocalTime;


public class ClothesFragment extends Fragment {
    private TextView timesOfDay;
    private TextView nameUser;
    private TextView dayWeather;
    private ClothesViewModel viewModel;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_clothes, container, false);
            timesOfDay=view.findViewById(R.id.TimesOfDay);
            nameUser=view.findViewById(R.id.nameUser);
            dayWeather=view.findViewById(R.id.dayWeather);
            LocalTime time=LocalTime.now();
            if(time.getHour()>0&&time.getHour()<9) timesOfDay.setText(getResources().getString(R.string.good_morning));
            else if(time.getHour()>9&&time.getHour()<16) timesOfDay.setText(getResources().getString(R.string.good_afternoon));
            else timesOfDay.setText(getResources().getString(R.string.good_evening));
            Bundle extras=getActivity().getIntent().getExtras();
            if(extras!=null) {
                String name= extras.getString(LoggedInFragment.NAME_EXTRA);
                nameUser.setText(name);
            }
            viewModel= ViewModelProviders.of(getActivity()).get(ClothesViewModel.class);
//            viewModel.getWeather().observe(getActivity(), new Observer<MainWeather>() {
//                @Override
//                public void onChanged(MainWeather mainWeather) {
//                    dayWeather.setText(Float.toString(mainWeather.getMain().getTemp()));
//                }
//            });
        return view;
    }
}