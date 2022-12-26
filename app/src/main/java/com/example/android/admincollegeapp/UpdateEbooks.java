package com.example.android.admincollegeapp;

import static android.content.Intent.createChooser;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UpdateEbooks<toast> extends AppCompatActivity {
    private CardView addPdf;
    private   int REQ = 1;
    private Uri pdfData;
    private String pdfName,title;
    private TextView pdftextView;
    private EditText PdfTitle;
    private Button UploadPdf;
    private ProgressDialog pd;
    private DatabaseReference DBreference;
    private com.google.firebase.storage.StorageReference StorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ebooks);
        DBreference = FirebaseDatabase.getInstance().getReference();
        StorageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);
        addPdf = findViewById(R.id.PickPDF);
        UploadPdf = findViewById(R.id.EbookAdd);
        PdfTitle = findViewById(R.id.ebooktitle);
        pdftextView=findViewById(R.id.PDFtextView);
       addPdf.setOnClickListener(view -> openGallery());
       UploadPdf.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               title=PdfTitle.getText().toString();
               if(title.isEmpty()){
                   PdfTitle.setError("Empty");
                   PdfTitle.requestFocus();

               }else if(pdfData==null){
                   Toast.makeText(UpdateEbooks.this,"Please Upload Pdf",Toast.LENGTH_SHORT).show();
               }else{
                   UploadPdf();
               }
           }

           private void UploadPdf() {
               pd.setTitle("Please Wait");
               pd.setMessage("Uploading Pdf.....");
               pd.show();

           StorageReference reference=StorageReference.child("Pdf/"+pdfName+"/"+System.currentTimeMillis()+".pdf");
           reference.putFile(pdfData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                   while(!uriTask.isComplete());
              Uri uri=uriTask.getResult();
              UploadData(String.valueOf(uri));
               }


           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   pd.dismiss();
                   Toast.makeText(UpdateEbooks.this , "Something is Wrong", Toast.LENGTH_SHORT).show();
               }
           });
           }
       });





    }

    private void UploadData(String valueOf) {

        String unqueKey= DBreference.child("pdf").push().getKey();
        HashMap data =new HashMap();
        data.put("PdfTitle",title);
        data.put("PdfUrl",valueOf);
        DBreference.child("Pdf").child(unqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UpdateEbooks.this , "pdf Uploaded Scucessfully ", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UpdateEbooks.this , "Failed to uload PDf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent= new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivity(Intent.createChooser(intent, "Open File Using..."));
        startActivityForResult(intent , REQ);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            pdfData = data.getData();
            if (pdfData.toString().startsWith("content://")) {
                Cursor cursor=null;

                try {
                    cursor= UpdateEbooks.this.getContentResolver().query(pdfData,null,null,null,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cursor != null && cursor.moveToFirst()) {
                    pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
                }
            } else if (pdfData.toString().startsWith("file://")) {
                pdfName=new File( pdfData.toString()).getName();
            }
        pdftextView.setText(pdfName);
        }
    }