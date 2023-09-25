package com.example.weatherappproject.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.weatherappproject.R;
import com.example.weatherappproject.adapters.ComplectAdapter;
import com.example.weatherappproject.model.Complect;
import com.example.weatherappproject.model.MainWeather;
import com.example.weatherappproject.viewmodel.DetailViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class DetailFragment extends Fragment {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CITY = "mycity";
    public static final String APP_PREFERENCES_AUTO = "auto";
    public static final String COMPLECT_EXTRA="complect";
    public static final String FRAGMENT_EXTRA="source";
    private String nameUserString;
    private String dayPart;
    private TextView nameUser;
    private TextView dayWeather;
    private DetailViewModel loggedInViewModel;
    private EditText geo;
    private ImageView imageWeather;
    private ToggleButton autoLocation;
    private RecyclerView rv;
    private Boolean autoLoc = false;
    private MutableLiveData<MainWeather> data;
    private SharedPreferences mySharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private DatabaseReference bd;
    private List<Complect> listComplect;
    private ComplectAdapter.OnComplectClickListener listener;
    private int currentWeather;
    @SuppressLint({"SuspiciousIndentation", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        nameUser = view.findViewById(R.id.nameUser);
        dayWeather = view.findViewById(R.id.dayWeather);
        geo = view.findViewById(R.id.geo);
        Button geo_button = view.findViewById(R.id.geoButton);
        imageWeather = view.findViewById(R.id.imageWeather);
        autoLocation = view.findViewById(R.id.geoloc);
        rv=view.findViewById(R.id.weatherComplect);
        LocalTime time = LocalTime.now();
        mySharedPreferences = requireActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (time.getHour() > 0 && time.getHour() < 9)
            dayPart = getResources().getString(R.string.good_morning);
        else if (time.getHour() > 9 && time.getHour() < 16)
            dayPart = getResources().getString(R.string.good_afternoon);
        else dayPart = getResources().getString(R.string.good_evening);
        mAuth = FirebaseAuth.getInstance();
        bd = FirebaseDatabase.getInstance().getReference();
        listComplect = new ArrayList<>();
        listener=new ComplectAdapter.OnComplectClickListener() {
            @Override
            public void onComplectClick(Complect complect, int position) {
                Intent intent=new Intent(getContext(),ComplectActivity.class);
                intent.putExtra(COMPLECT_EXTRA,complect);
                intent.putExtra(FRAGMENT_EXTRA,"detail");
                getContext().startActivity(intent);
            }
        };
        loggedInViewModel = new ViewModelProvider(Objects.requireNonNull(requireActivity())).get(DetailViewModel.class);
        loggedInViewModel.getUserLiveData().observe(getActivity(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("Users");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot datas : snapshot.getChildren()) {
                                String email = datas.child("email").getValue().toString();
                                if (email.equals(firebaseUser.getEmail())) {
                                    nameUserString = datas.child("firstname").getValue().toString();
                                    nameUser.setText(dayPart + ", " + nameUserString);
                                    break;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        if (mySharedPreferences.contains(APP_PREFERENCES_AUTO)) {
            autoLoc = mySharedPreferences.getBoolean(APP_PREFERENCES_AUTO, false);
            autoLocation.setChecked(autoLoc);
        }
        if (autoLoc)
        {
            if (!(ActivityCompat.checkSelfPermission(getActivity() ,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
                            String city=addresses.get(0).getLocality();
                            geo.setText(city);
                            data = loggedInViewModel.getWeather(city);
                            data.observe(requireActivity(), new Observer<MainWeather>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onChanged(MainWeather mainWeather) {
                                    if (mainWeather != null) {
                                        dayWeather.setText(Integer.toString(Math.round(mainWeather.getMain().getTemp())) + " C");
                                        currentWeather=Math.round(mainWeather.getMain().getTemp());
                                        String icon = "https://openweathermap.org/img/wn/" + mainWeather.getWeather()[0].getIcon() + ".png";
                                        Picasso.get().load(icon).into(imageWeather);
                                        getCloses();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        else
        {
            if (mySharedPreferences.contains(APP_PREFERENCES_CITY)) {
                data = loggedInViewModel.getWeather(mySharedPreferences.getString(APP_PREFERENCES_CITY, ""));
                geo.setText(mySharedPreferences.getString(APP_PREFERENCES_CITY, ""));
                data.observe(requireActivity(), new Observer<MainWeather>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(MainWeather mainWeather) {
                        if (mainWeather != null) {
                            dayWeather.setText(Integer.toString(Math.round(mainWeather.getMain().getTemp())) + " C");
                            currentWeather=Math.round(mainWeather.getMain().getTemp());
                            String icon = "https://openweathermap.org/img/wn/" + mainWeather.getWeather()[0].getIcon() + ".png";
                            Picasso.get().load(icon).into(imageWeather);
                            getCloses();
                        }
                    }
                });
            }
        }
        autoLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                autoLoc = autoLocation.isChecked();
                editor = mySharedPreferences.edit();
                editor.putBoolean(APP_PREFERENCES_AUTO, autoLoc);
                editor.apply();
                if (ActivityCompat.checkSelfPermission(getActivity() ,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Geocoder geocoder = new Geocoder(getActivity());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
                            String city=addresses.get(0).getLocality();
                            geo.setText(city);
                            data = loggedInViewModel.getWeather(city);
                            data.observe(requireActivity(), new Observer<MainWeather>() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onChanged(MainWeather mainWeather) {
                                    if (mainWeather != null) {
                                        dayWeather.setText(Integer.toString(Math.round(mainWeather.getMain().getTemp())) + " C");
                                        currentWeather=Math.round(mainWeather.getMain().getTemp());
                                        String icon = "https://openweathermap.org/img/wn/" + mainWeather.getWeather()[0].getIcon() + ".png";
                                        Picasso.get().load(icon).into(imageWeather);
                                        getCloses();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        geo_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                autoLocation.setChecked(false);
                autoLoc = autoLocation.isChecked();
                editor = mySharedPreferences.edit();
                editor.putBoolean(APP_PREFERENCES_AUTO, autoLoc);
                editor.apply();
                data = loggedInViewModel.getWeather(geo.getText().toString());
                data.observe(requireActivity(), new Observer<MainWeather>()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onChanged(MainWeather mainWeather) {
                        if (mainWeather != null) {
                            dayWeather.setText(Integer.toString(Math.round(mainWeather.getMain().getTemp())) + " C");
                            currentWeather=Math.round(mainWeather.getMain().getTemp());
                            String icon = "https://openweathermap.org/img/wn/" + mainWeather.getWeather()[0].getIcon() + ".png";
                            Picasso.get().load(icon).into(imageWeather);
                            getCloses();
                        }
                    }
                });

                if (data != null) {
                    editor = mySharedPreferences.edit();
                    editor.putString(APP_PREFERENCES_CITY, geo.getText().toString());
                    editor.apply();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);
        ActivityResultLauncher<String[]> locationPermissionRequest=
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),result -> {
                    Boolean fileLocationGranted=result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false);
                    Boolean coarseLocationGranted=result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false);
                    if(fileLocationGranted!=null&&fileLocationGranted)
                    {

                    }
                    else if(coarseLocationGranted!=null&&coarseLocationGranted)
                    {

                    }
                    else {

                    }
                });
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
    private void getCloses()
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            Query users = bd.child("Users");
            users.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for (DataSnapshot postSnapshot : snapshot.getChildren())
                    {
                        if (firebaseUser.getEmail().equals(postSnapshot.child("email").getValue().toString()))
                        {
                            String email = postSnapshot.child("email").getValue().toString();
                            Query complects = bd.child("Complects");
                            complects.addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    listComplect.clear();

                                    for (DataSnapshot postSnapshot : snapshot.getChildren())
                                    {
                                        String current=(postSnapshot.child("email").getValue()!=null)?postSnapshot.child("email").getValue().toString():"";
                                        if (email.equals(current))
                                        {
                                            Complect complect = new Complect();
                                            complect.setKey(postSnapshot.getKey());
                                            complect.setFootwear(postSnapshot.child("Footwear").getValue().toString());
                                            complect.setHeadgear(postSnapshot.child("Headgear").getValue().toString());
                                            complect.setOuterwear(postSnapshot.child("Outerwear").getValue().toString());
                                            complect.setPants(postSnapshot.child("Pants").getValue().toString());
                                            complect.setShirt(postSnapshot.child("Shirt").getValue().toString());
                                            complect.setEmail(email);
                                            complect.setTemp1(Integer.parseInt((postSnapshot.child("temp1").getValue()!=null)?postSnapshot.child("temp1").getValue().toString():"0"));
                                            complect.setTemp2(Integer.parseInt((postSnapshot.child("temp2").getValue()!=null)?postSnapshot.child("temp1").getValue().toString():"0"));
                                            if(currentWeather>=complect.getTemp1()&&currentWeather<=complect.getTemp2())
                                                listComplect.add(complect);
                                        }
                                    }
                                    ComplectAdapter adapter = new ComplectAdapter(listComplect, listener);
                                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                                    rv.setAdapter(adapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }
    }
}