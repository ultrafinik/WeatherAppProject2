package com.example.weatherappproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.weatherappproject.R;
import com.example.weatherappproject.model.Complect;
import com.squareup.picasso.Picasso;

public class ComplectActivity extends AppCompatActivity {
    private ImageView footwearImage,headerImage, outwearImage,pantsImage,shirtImage;
    private Button buttonFootwear;
    private String source;
    private Complect complect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_complect);
        footwearImage = findViewById(R.id.footwear_image);
        headerImage = findViewById(R.id.headgear_image);
        outwearImage = findViewById(R.id.outerwear_image);
        pantsImage = findViewById(R.id.pants_image);
        shirtImage = findViewById(R.id.shirt_image);
        buttonFootwear=findViewById(R.id.button_footwear);
        Bundle arg=getIntent().getExtras();
        if(arg!=null)
        {
            complect = (Complect) arg.getSerializable(DetailFragment.COMPLECT_EXTRA);
            source = arg.getString(DetailFragment.FRAGMENT_EXTRA, "");
            if (complect != null)
            {
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
        buttonFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complect != null)
                {

                }
            }
        });
    }
}