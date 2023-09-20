package com.example.weatherappproject.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.weatherappproject.R;
import com.example.weatherappproject.model.Complect;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ComplectActivity extends AppCompatActivity {
    public static final String FOOT_WEAR="foot_wear";
    private ImageButton headerImage, outwearImage,pantsImage,shirtImage, buttonFootwear;
    private String source;
    private Complect complect;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_complect);
        buttonFootwear = findViewById(R.id.footwear_image);
        headerImage = findViewById(R.id.headgear_image);
        outwearImage = findViewById(R.id.outerwear_image);
        pantsImage = findViewById(R.id.pants_image);
        shirtImage = findViewById(R.id.shirt_image);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Bundle arg=getIntent().getExtras();
        if(arg!=null)
        {
            complect = (Complect) arg.getSerializable(DetailFragment.COMPLECT_EXTRA);
            source = arg.getString(DetailFragment.FRAGMENT_EXTRA, "");
            if (complect != null)
            {
                if (!complect.getFootwear().equals("")) {
                    Picasso.get().load(complect.getFootwear()).into(buttonFootwear);
                }
                if (!complect.getHeadgear().equals("")) {
                    Picasso.get().load(complect.getHeadgear()).into(headerImage);
                }
                if (!complect.getOuterwear().equals("")) {
                    Picasso.get().load(complect.getOuterwear()).into(outwearImage);
                }
                if (!complect.getPants().equals("")) {
                    Picasso.get().load(complect.getPants()).into(pantsImage);
                }
                if (!complect.getFootwear().equals("")) {
                    Picasso.get().load(complect.getShirt()).into(shirtImage);
                }
            }
        }
        buttonFootwear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complect != null)
                {
                    Intent intent=new Intent(ComplectActivity.this,AddEditComplectActivity.class);
                    intent.putExtra(FOOT_WEAR,complect.getFootwear());
                    footWearResult.launch(intent);
                }
            }
        });
    }
    private void uploadImage(byte[] data)
    {
        String uid=UUID.randomUUID().toString();
        UploadTask uploadTask = storageReference.child(uid).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // если произошла ошибка
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String res=storageReference.child(uid).getPath();
                int i=0;
            }
        });
    }
    ActivityResultLauncher<Intent> footWearResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        filePath=result.getData().getData();
                        Bundle extras= result.getData().getExtras();
                        Bitmap picture=(Bitmap)extras.get("data");
                        picture=Bitmap.createScaledBitmap(picture,picture.getWidth()*3,picture.getHeight()*3,true);
                        buttonFootwear.setImageBitmap(picture);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        uploadImage(data);
                    }
                }
            });
}