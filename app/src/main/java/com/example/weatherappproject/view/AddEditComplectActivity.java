package com.example.weatherappproject.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.weatherappproject.R;
import com.squareup.picasso.Picasso;

public class AddEditComplectActivity extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private ImageView fotoImage;
    private Button cameraButton,galaryButton,saveButton;
    private Intent intent;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_complect);
        fotoImage=findViewById(R.id.fotoImage);
        cameraButton=findViewById(R.id.camera);
        galaryButton=findViewById(R.id.galary);
        saveButton=findViewById(R.id.yesfoto);
        saveButton.setVisibility(View.INVISIBLE);
        if(getIntent().getExtras()!=null)
        {
            String url=getIntent().getExtras().getString(ComplectActivity.FOOT_WEAR);
            Picasso.get().load(url).into(fotoImage);
        }
        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            intent=result.getData();
                            Bundle extras=result.getData().getExtras();
                            saveButton.setVisibility(View.VISIBLE);
                            Bitmap foto=(Bitmap)extras.get("data");
                            fotoImage.setImageBitmap(foto);
                        }
                    }
                });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    mStartForResult.launch(takePictureIntent);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}