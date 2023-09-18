package com.example.weatherappproject.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.weatherappproject.R;
import com.example.weatherappproject.model.Complect;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class ComplectFragment extends Fragment {
    private ImageView footwearImage,headerImage, outwearImage,pantsImage,shirtImage;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        Complect complect = (Complect) getArguments().getSerializable(DetailFragment.COMPLECT_EXTRA);
        if(complect !=null) {

            if (!complect.getFootwear().equals("")) {
                footwearImage.setVisibility(View.VISIBLE);
                Picasso.get().load(complect.getFootwear()).into(footwearImage);
            }
            if (!complect.getHeadgear().equals("")) {
                headerImage.setVisibility(View.VISIBLE);
                Picasso.get().load(complect.getHeadgear()).into(headerImage);
            }
            if (!complect.getOuterwear().equals("")) {
                outwearImage.setVisibility(View.VISIBLE);
                Picasso.get().load(complect.getOuterwear()).into(outwearImage);
            }
            if (!complect.getPants().equals("")) {
                pantsImage.setVisibility(View.VISIBLE);
                Picasso.get().load(complect.getPants()).into(pantsImage);
            }
            if (!complect.getFootwear().equals("")) {
                shirtImage.setVisibility(View.VISIBLE);
                Picasso.get().load(complect.getShirt()).into(shirtImage);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_complect, container, false);
        footwearImage = view.findViewById(R.id.footwear_image);
        headerImage = view.findViewById(R.id.headgear_image);
        outwearImage = view.findViewById(R.id.outerwear_image);
        pantsImage = view.findViewById(R.id.pants_image);
        shirtImage = view.findViewById(R.id.shirt_image);

        Button ok_button = view.findViewById(R.id.ok_button);
        ok_button.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_ComplectFragment_to_DetailFragment));
        //NavigationUI.setupWithNavController(toolbar,Navigation.findNavController(Objects.requireNonNull(getView())));
        return  view;
    }
}