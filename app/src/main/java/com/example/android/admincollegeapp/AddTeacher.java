package com.example.android.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
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

public class AddTeacher extends AppCompatActivity {
    private ImageView avtar;
    private EditText addTeacherName,addTeacherEmail,addTeacherPhone;
    private Button addteacherBtn;
    private final int REQ=1;
    private Bitmap bitmap=null;
    private DatabaseReference reference,DBref;
    private StorageReference StorageReference;
    private String Downloadurl="",name,email,phone,Category;
    private Spinner addteacherCategory;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        avtar=findViewById(R.id.addTeacherImage);
        addTeacherEmail=findViewById(R.id.addTeacherEmail);
        addTeacherPhone=findViewById(R.id.addTeacherMobile);
        addTeacherName=findViewById(R.id.addTeacherName);
        addteacherCategory=findViewById(R.id.Teacher_Category);
        reference= FirebaseDatabase.getInstance().getReference("Faculty");
        StorageReference= FirebaseStorage.getInstance().getReference();
        addteacherBtn=findViewById(R.id.addteacherbtn);
        pd= new ProgressDialog(this);
        String[] Items= new String[]{"Select Category","Science And Humanity"};//,"CSE","Pharmacy","Mechanical"
        addteacherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,Items));
        addteacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category=addteacherCategory.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        avtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        addteacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckValidation();
            }
        });
    }

    private void CheckValidation() {
        name=addTeacherName.getText().toString();
        email=addTeacherEmail.getText().toString();
        phone=addTeacherPhone.getText().toString();

        if(name.isEmpty()){
            addTeacherName.setError("Empty");
            addTeacherName.requestFocus();
        }else if(email.isEmpty()){
            addTeacherEmail.setError("Empty");
            addTeacherEmail.requestFocus();
        }else if(phone.isEmpty()){
            addTeacherPhone.setError("Empty");
            addTeacherPhone.requestFocus();
        }else if(Category.equals("Select Category")){
            Toast.makeText(this, "Please Provide Teacher Catagory", Toast.LENGTH_SHORT).show();
//        }else if(bitmap==null) {
//            pd.setMessage("Uploading...");
//            pd.show();
//            insertImage();
        }else{
            pd.setMessage("Uploading...");
            pd.show();
            insertImage();
        }
    }

    private void insertImage() {
        ByteArrayOutputStream Baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, Baos);
        byte[] finalIMage = Baos.toByteArray();
        final StorageReference filepath;
        filepath = StorageReference.child("Teachers").child(finalIMage + "jpg");
        final UploadTask uploadTask = filepath.putBytes(finalIMage);
        uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Downloadurl = String.valueOf(uri);
                                    insertData();
                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(AddTeacher.this, "something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void insertData() {

        reference=reference.child(Category);
        final String UniqueKey = reference.push().getKey();
        TeacherData teacherData= new TeacherData(name,email,phone,Downloadurl,UniqueKey);
        reference.child(UniqueKey).setValue(teacherData ).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddTeacher.this, "Teacher Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "SomeThing went Wrong", Toast.LENGTH_SHORT).show();
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
            avtar.setImageBitmap(bitmap);
        }
    }
}