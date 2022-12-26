package com.example.android.admincollegeapp;

import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
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

public class UpdateNotice extends AppCompatActivity {
    private CardView addNoticeImage;
    private final int REQ=1;
    private Bitmap bitmap;
    private EditText NoticeTitle;
    private Button UploadNotice;
    private ImageView NoticeImage;
    private ProgressDialog pd;
    String Downloadurl="";
    private DatabaseReference reference,DBRef;
    private StorageReference StorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_update);
        reference= FirebaseDatabase.getInstance().getReference();
        StorageReference= FirebaseStorage.getInstance().getReference();
        NoticeImage = findViewById(R.id.noticeImageView);
        pd=new ProgressDialog(this);
        addNoticeImage=findViewById(R.id.addImage);
        UploadNotice=findViewById(R.id.noticeUpload);
        NoticeTitle=findViewById(R.id.noticetitle);
        addNoticeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        UploadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NoticeTitle.getText().toString().isEmpty()){
                    Toast.makeText(UpdateNotice.this, "Enter Title", Toast.LENGTH_SHORT).show();
                    NoticeTitle.setError("Empty");
                    NoticeTitle.requestFocus();
                }else if(bitmap==null){
                    uploadData();
                }else{
                    UploadImage();

                }
            }
        });

    }
    private void UploadImage() {
        pd.setMessage("Uploading....");
        pd.show();
        ByteArrayOutputStream Baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,Baos);
        byte[] finalIMage=Baos.toByteArray();
        final StorageReference filepath;
        filepath= StorageReference.child("notice").child(finalIMage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalIMage);
        uploadTask.addOnCompleteListener(UpdateNotice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
               Toast.makeText(UpdateNotice.this,"something went Wrong",Toast.LENGTH_SHORT).show();
           }
            }
        });
    }
    private void uploadData() {
        DBRef=reference.child("Notice");
        final String UniqueKey = DBRef.push().getKey();
        String title=NoticeTitle.getText().toString();

        Calendar CalForDate= Calendar.getInstance();
        SimpleDateFormat Currentdate= new SimpleDateFormat("dd-MM-yy");
        String Date=Currentdate.format(CalForDate.getTime());
        Calendar CalForTime=Calendar.getInstance();
        SimpleDateFormat CurrentTime= new SimpleDateFormat("hh:mm a");
        String Time=CurrentTime.format(CalForTime.getTime());
        NoticeData noticeData= new NoticeData(title,Downloadurl,Date,Time,UniqueKey);
        DBRef.child(UniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pd.dismiss();
                Toast.makeText(UpdateNotice.this, "Notice Uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpdateNotice.this, "SomeThing went Wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }



    private void OpenGallery() {
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
            NoticeImage.setImageBitmap(bitmap);
        }
    }

}