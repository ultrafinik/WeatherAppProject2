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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.weatherappproject.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddEditComplectActivity extends AppCompatActivity {

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
            String url="";
            if(getIntent().getExtras().getString(ComplectActivity.FOOT_WEAR)!=null)
                url=getIntent().getExtras().getString(ComplectActivity.FOOT_WEAR,"");
            if(getIntent().getExtras().getString(ComplectActivity.HEAD_GEAR)!=null)
                url=getIntent().getExtras().getString(ComplectActivity.HEAD_GEAR,"");
            if(getIntent().getExtras().getString(ComplectActivity.OUTER_WEAR)!=null)
                url=getIntent().getExtras().getString(ComplectActivity.OUTER_WEAR,"");
            if(getIntent().getExtras().getString(ComplectActivity.PANTS)!=null)
                url=getIntent().getExtras().getString(ComplectActivity.PANTS,"");
            if(getIntent().getExtras().getString(ComplectActivity.SHIRT)!=null)
                url=getIntent().getExtras().getString(ComplectActivity.SHIRT,"");
            if(!url.equals(""))
                Picasso.get().load(url).into(fotoImage);
        }
        ActivityResultLauncher<Intent> startForResultCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            intent=result.getData();
                            intent.putExtra("content","camera");
                            Bundle extras=result.getData().getExtras();
                            saveButton.setVisibility(View.VISIBLE);
                            Bitmap foto=(Bitmap)extras.get("data");
                            fotoImage.setImageBitmap(foto);
                        }
                    }
                });
        ActivityResultLauncher<Intent> startForResultGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>()
                {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            try
                            {
                                intent=result.getData();
                                intent.putExtra("content","galery");
                                final InputStream imageStream = getContentResolver().openInputStream(intent.getData());
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                saveButton.setVisibility(View.VISIBLE);
                                fotoImage.setImageBitmap(selectedImage);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }
                });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startForResultCamera.launch(takePictureIntent);
                } catch (ActivityNotFoundException e) {
                }
            }
        });
        galaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(Intent.ACTION_PICK);
                takePictureIntent.setType("image/*");
                try {
                    startForResultGallery.launch(takePictureIntent);
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