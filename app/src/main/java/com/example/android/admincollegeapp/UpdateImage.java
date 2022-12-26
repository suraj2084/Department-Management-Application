package com.example.android.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateImage extends AppCompatActivity {
    private Spinner ImageCategory;
    private CardView SelectImage;
    private final int REQ=1;
    private Bitmap bitmap;
    private Button UploadImage;
    private DatabaseReference reference;
    private StorageReference StorageReference;
    String Downloadurl="";
    private ImageView GalleryImage;
    private String Category;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_image);
        ImageCategory=findViewById(R.id.Image_Category);
        SelectImage=findViewById(R.id.addGalleryImage);
        UploadImage=findViewById(R.id.UploadImageBtn);
        GalleryImage=findViewById(R.id.GalleryImageView);
        reference= FirebaseDatabase.getInstance().getReference().child("Gallery");
        StorageReference= FirebaseStorage.getInstance().getReference().child("Gallery");
        pd= new ProgressDialog(this);

        String[] Items= new String[]{"Select Category","Convocation","Independance Day","Youth Day","Freshers 2022","Other Event"};
        ImageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,Items));
        ImageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 Category=ImageCategory.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap == null){
                    Toast.makeText(UpdateImage.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }else if(Category.equals("Select Category")){
                    Toast.makeText(UpdateImage.this, "Please Select Image Category", Toast.LENGTH_SHORT).show();
                }else {
                    pd.setMessage("Uploading...");
                    pd.show();
                    UploadImage();
                }
            }
        });

    }

    private void UploadImage() {
        ByteArrayOutputStream Baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,Baos);
        byte[] finalIMage=Baos.toByteArray();
        final StorageReference filepath;
        filepath= StorageReference.child(finalIMage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalIMage);
        uploadTask.addOnCompleteListener(UpdateImage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Downloadurl=String.valueOf(uri);
                                    uploadData();
                                }
                            });
                        }
                    });
                }else{
                    pd.dismiss();
                    Toast.makeText(UpdateImage.this,"something went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadData() {
        reference=reference.child(Category);
        final String UniqueKey = reference.push().getKey();

//        Calendar CalForDate= Calendar.getInstance();
//        SimpleDateFormat Currentdate= new SimpleDateFormat("dd-MM-yy");
//        String Date=Currentdate.format(CalForDate.getTime());
//        Calendar CalForTime=Calendar.getInstance();
//        SimpleDateFormat CurrentTime= new SimpleDateFormat("hh:mm a");
//        String Time=CurrentTime.format(CalForTime.getTime());
//        NoticeData noticeData= new NoticeData(title,Downloadurl,Date,Time,UniqueKey);
        reference.child(UniqueKey).setValue(Downloadurl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UpdateImage.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpdateImage.this, "SomeThing went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent pickImage= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQ && resultCode== RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            GalleryImage.setImageBitmap(bitmap);
        }
    }
}