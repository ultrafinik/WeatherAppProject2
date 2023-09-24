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
import android.graphics.BitmapFactory;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.UUID;

public class ComplectActivity extends AppCompatActivity {
    public static final String FOOT_WEAR="foot_wear";
    public static final String HEAD_GEAR="head_gear";
    public static final String OUTER_WEAR="outer_wear";
    public static final String PANTS="pants";
    public static final String SHIRT="shirt";
    private ImageButton headerImage, outwearImage,pantsImage,shirtImage, buttonFootwear;
    private String source;
    private Complect complect;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseDatabase bd;
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
        bd = FirebaseDatabase.getInstance();
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
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complect != null)
                {
                    Intent intent=new Intent(ComplectActivity.this,AddEditComplectActivity.class);
                    intent.putExtra(HEAD_GEAR,complect.getHeadgear());
                    headGearResult.launch(intent);
                }
            }
        });
        outwearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complect != null)
                {
                    Intent intent=new Intent(ComplectActivity.this,AddEditComplectActivity.class);
                    intent.putExtra(OUTER_WEAR,complect.getOuterwear());
                    outwearResult.launch(intent);
                }
            }
        });
        pantsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complect != null)
                {
                    Intent intent=new Intent(ComplectActivity.this,AddEditComplectActivity.class);
                    intent.putExtra(PANTS,complect.getPants());
                    pantsResult.launch(intent);
                }
            }
        });
        shirtImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complect != null)
                {
                    Intent intent=new Intent(ComplectActivity.this,AddEditComplectActivity.class);
                    intent.putExtra(PANTS,complect.getShirt());
                    shirtResult.launch(intent);
                }
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
                        String content=extras.getString("content");
                        Bitmap picture=null;
                        if(content.equals("camera")) {
                            picture = (Bitmap) extras.get("data");
                            picture=Bitmap.createScaledBitmap(picture,picture.getWidth()*3,picture.getHeight()*3,true);
                        }
                        else
                        {
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(filePath);
                                picture = BitmapFactory.decodeStream(imageStream);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        buttonFootwear.setImageBitmap(picture);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String uid=UUID.randomUUID().toString();
                        UploadTask uploadTask = storageReference.child(uid).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String uri="https://firebasestorage.googleapis.com"+taskSnapshot.getUploadSessionUri().getPath()+"/"+uid+
                                        "?alt=media";
                                DatabaseReference chooseComplect = bd.getReference().child("Complects");
                                chooseComplect.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        DataSnapshot datas=snapshot.child(complect.getKey());
                                        DatabaseReference a=datas.child("Footwear").getRef();
                                        complect.setFootwear(uri);
                                        a.setValue(uri);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                }
            });
    ActivityResultLauncher<Intent> headGearResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        filePath=result.getData().getData();
                        Bundle extras= result.getData().getExtras();
                        String content=extras.getString("content");
                        Bitmap picture=null;
                        if(content.equals("camera")) {
                            picture = (Bitmap) extras.get("data");
                            picture=Bitmap.createScaledBitmap(picture,picture.getWidth()*3,picture.getHeight()*3,true);
                        }
                        else
                        {
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(filePath);
                                picture = BitmapFactory.decodeStream(imageStream);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        headerImage.setImageBitmap(picture);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String uid=UUID.randomUUID().toString();
                        UploadTask uploadTask = storageReference.child(uid).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String uri="https://firebasestorage.googleapis.com"+taskSnapshot.getUploadSessionUri().getPath()+"/"+uid+
                                        "?alt=media";
                                DatabaseReference chooseComplect = bd.getReference().child("Complects");
                                chooseComplect.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        DataSnapshot datas=snapshot.child(complect.getKey());
                                        DatabaseReference a=datas.child("Headgear").getRef();
                                        complect.setHeadgear(uri);
                                        a.setValue(uri);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                }
            });
    ActivityResultLauncher<Intent> outwearResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        filePath=result.getData().getData();
                        Bundle extras= result.getData().getExtras();
                        String content=extras.getString("content");
                        Bitmap picture=null;
                        if(content.equals("camera")) {
                            picture = (Bitmap) extras.get("data");
                            picture=Bitmap.createScaledBitmap(picture,picture.getWidth()*3,picture.getHeight()*3,true);
                        }
                        else
                        {
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(filePath);
                                picture = BitmapFactory.decodeStream(imageStream);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        outwearImage.setImageBitmap(picture);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String uid=UUID.randomUUID().toString();
                        UploadTask uploadTask = storageReference.child(uid).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String uri="https://firebasestorage.googleapis.com"+taskSnapshot.getUploadSessionUri().getPath()+"/"+uid+
                                        "?alt=media";
                                DatabaseReference chooseComplect = bd.getReference().child("Complects");
                                chooseComplect.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        DataSnapshot datas=snapshot.child(complect.getKey());
                                        DatabaseReference a=datas.child("Outerwear").getRef();
                                        complect.setOuterwear(uri);
                                        a.setValue(uri);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                }
            });
    ActivityResultLauncher<Intent> pantsResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        filePath=result.getData().getData();
                        Bundle extras= result.getData().getExtras();
                        String content=extras.getString("content");
                        Bitmap picture=null;
                        if(content.equals("camera")) {
                            picture = (Bitmap) extras.get("data");
                            picture=Bitmap.createScaledBitmap(picture,picture.getWidth()*3,picture.getHeight()*3,true);
                        }
                        else
                        {
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(filePath);
                                picture = BitmapFactory.decodeStream(imageStream);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        pantsImage.setImageBitmap(picture);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String uid=UUID.randomUUID().toString();
                        UploadTask uploadTask = storageReference.child(uid).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String uri="https://firebasestorage.googleapis.com"+taskSnapshot.getUploadSessionUri().getPath()+"/"+uid+
                                        "?alt=media";
                                DatabaseReference chooseComplect = bd.getReference().child("Complects");
                                chooseComplect.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        DataSnapshot datas=snapshot.child(complect.getKey());
                                        DatabaseReference a=datas.child("Pants").getRef();
                                        complect.setPants(uri);
                                        a.setValue(uri);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                }
            });
    ActivityResultLauncher<Intent> shirtResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        filePath=result.getData().getData();
                        Bundle extras= result.getData().getExtras();
                        String content=extras.getString("content");
                        Bitmap picture=null;
                        if(content.equals("camera")) {
                            picture = (Bitmap) extras.get("data");
                            picture=Bitmap.createScaledBitmap(picture,picture.getWidth()*3,picture.getHeight()*3,true);
                        }
                        else
                        {
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(filePath);
                                picture = BitmapFactory.decodeStream(imageStream);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        shirtImage.setImageBitmap(picture);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();
                        String uid=UUID.randomUUID().toString();
                        UploadTask uploadTask = storageReference.child(uid).putBytes(data);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                String uri="https://firebasestorage.googleapis.com"+taskSnapshot.getUploadSessionUri().getPath()+"/"+uid+
                                        "?alt=media";
                                DatabaseReference chooseComplect = bd.getReference().child("Complects");
                                chooseComplect.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        DataSnapshot datas=snapshot.child(complect.getKey());
                                        DatabaseReference a=datas.child("Shirt").getRef();
                                        complect.setShirt(uri);
                                        a.setValue(uri);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }
                }
            });
}