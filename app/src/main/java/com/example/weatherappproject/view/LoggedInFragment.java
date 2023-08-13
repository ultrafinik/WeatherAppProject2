package com.example.weatherappproject.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.weatherappproject.model.MainWeather;
import com.example.weatherappproject.viewmodel.LoggedInViewModel;
import com.example.weatherappproject.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.util.Objects;

public class LoggedInFragment extends Fragment {
    private String nameUserString;
    private TextView nameUser;
    private TextView dayWeather;
    private Button logOutButton;
    private LoggedInViewModel loggedInViewModel;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loggedin, container, false);
            logOutButton = view.findViewById(R.id.fragment_loggedin_logOut);
            TextView timesOfDay = view.findViewById(R.id.TimesOfDay);
            nameUser=view.findViewById(R.id.nameUser);
            dayWeather=view.findViewById(R.id.dayWeather);
            LocalTime time=LocalTime.now();
            if(time.getHour()>0&&time.getHour()<9) timesOfDay.setText(getResources().getString(R.string.good_morning));
            else if(time.getHour()>9&&time.getHour()<16) timesOfDay.setText(getResources().getString(R.string.good_afternoon));
            else timesOfDay.setText(getResources().getString(R.string.good_evening));
            Bundle extras=getActivity().getIntent().getExtras();
            loggedInViewModel= new ViewModelProvider(getActivity()).get(LoggedInViewModel.class);
            loggedInViewModel.getUserLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
                @Override
                public void onChanged(FirebaseUser firebaseUser) {
                    if (firebaseUser != null) {
                        logOutButton.setEnabled(true);
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
                                        nameUser.setText(nameUserString);
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        logOutButton.setEnabled(false);
                    }
                }
            });

            loggedInViewModel.getLoggedOutLiveData().observe(getActivity(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean loggedOut) {
                    if (loggedOut) {
                        Toast.makeText(getContext(), "User Logged Out", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).navigate(R.id.action_loggedInFragment_to_loginRegisterFragment);
                    }
                }
            });
                LiveData<MainWeather> data=loggedInViewModel.getWeather();
                data.observe(getActivity(), new Observer<MainWeather>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(MainWeather mainWeather) {
                        dayWeather.setText(Float.toString(mainWeather.getMain().getTemp()));
                    }
                });
                logOutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loggedInViewModel.logOut();
                    }
                });
            return view;
        }
}
