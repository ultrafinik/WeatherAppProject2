package com.example.weatherappproject.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weatherappproject.R;
import com.example.weatherappproject.model.MainWeather;
import com.example.weatherappproject.viewmodel.DetailViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;

public class DetailFragment extends Fragment {

    private String nameUserString;
    private String dayPart;
    private TextView nameUser;
    private TextView dayWeather;
    private DetailViewModel loggedInViewModel;
    private EditText geo;
    private Button geo_button;
    private LiveData<MainWeather> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint({"SuspiciousIndentation", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        nameUser=view.findViewById(R.id.nameUser);
        dayWeather=view.findViewById(R.id.dayWeather);
        geo=view.findViewById(R.id.geo);
        geo_button=view.findViewById(R.id.geoButton);
        LocalTime time=LocalTime.now();
        if(time.getHour()>0&&time.getHour()<9) dayPart=getResources().getString(R.string.good_morning);
            else if(time.getHour()>9&&time.getHour()<16) dayPart=getResources().getString(R.string.good_afternoon);
            else dayPart=getResources().getString(R.string.good_evening);
            loggedInViewModel= new ViewModelProvider(getActivity()).get(DetailViewModel.class);
            loggedInViewModel.getUserLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
                @Override
                public void onChanged(FirebaseUser firebaseUser)
                {
                    if (firebaseUser != null)
                    {
                        FirebaseDatabase database=FirebaseDatabase.getInstance();
                        DatabaseReference myRef=database.getReference().child("users");
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot datas: snapshot.getChildren())
                                {
                                    String email=datas.child("email").getValue().toString();
                                    if(email.equals(firebaseUser.getEmail()))
                                    {
                                        nameUserString=datas.child("name").getValue().toString();
                                        nameUser.setText(dayPart+", "+nameUserString);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });
            data = loggedInViewModel.getWeather("Калининград");
            geo_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data = loggedInViewModel.getWeather(geo.getText().toString());

                }
            });
            data.observe(getActivity(), new Observer<MainWeather>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onChanged(MainWeather mainWeather) {
                    dayWeather.setText(Integer.toString(Math.round(mainWeather.getMain().getTemp())));
                }
            });


        return view;
    }
}