package com.example.weatherappproject.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherappproject.viewmodel.DetailViewModel;
import com.example.weatherappproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoggedInFragment extends Fragment  implements BottomNavigationView
        .OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private DetailFragment detailFragment;
    private AllFragment allFragment;
    private ProfileFragment profileFragment;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_loggedin, container, false);
                bottomNavigationView = view.findViewById(R.id.bottomNavigation);
                detailFragment=new DetailFragment();
                profileFragment= new ProfileFragment();
                allFragment= new AllFragment();
                bottomNavigationView.setOnNavigationItemSelectedListener(this);
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.flFragment,detailFragment).commit();
            return view;
        }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.page_detail:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,detailFragment).commit();
                return true;
            case R.id.page_all:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,allFragment).commit();
                return true;
            case R.id.page_profile:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,profileFragment).commit();
                return true;

        }
        return false;
    }
}
