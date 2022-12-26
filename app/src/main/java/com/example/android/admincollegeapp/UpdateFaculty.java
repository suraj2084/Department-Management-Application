package com.example.android.admincollegeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView MCADepartment,PharmacyDepartment,CSEDepartment;
    private LinearLayout CSENoData,PharmacyNoData,MCANoData;
    private List<TeacherData> list1,list2,list3;
    private DatabaseReference reference;
    private TeacherAdpter Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
//        CSENoData=findViewById(R.id.CSENoData);
//        PharmacyNoData=findViewById(R.id.MtechNoData);
        MCANoData=findViewById(R.id.MCANoData);
        MCADepartment=findViewById(R.id.MCADepartment);
//        PharmacyDepartment=findViewById(R.id.MtechDepartment);
//        CSEDepartment=findViewById(R.id.CSEDepartment);
        reference= FirebaseDatabase.getInstance().getReference().child("Faculty");
        MCADepartment();
//        PharmacyDepartment();
//        CSEDepartment();
        fab=findViewById(R.id.feb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateFaculty.this,AddTeacher.class));
            }
        });

    }

    private void MCADepartment() {
        reference=reference.child("Science And Humanity");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1=new ArrayList<>();
                if (!snapshot.exists()){
                    MCANoData.setVisibility(View.VISIBLE);
                    MCADepartment.setVisibility(View.GONE);
                }else{
                    MCANoData.setVisibility(View.GONE);
                    MCADepartment.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot1: snapshot.getChildren() ){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    MCADepartment.setHasFixedSize(true);
                    MCADepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    Adapter=new TeacherAdpter(list1,UpdateFaculty.this);
                    MCADepartment.setAdapter(Adapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(),  Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    private void PharmacyDepartment() {
//        reference=reference.child("Mtech");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list2=new ArrayList<>();
//                if (!snapshot.exists()){
//                    PharmacyNoData.setVisibility(View.VISIBLE);
//                    PharmacyDepartment.setVisibility(View.GONE);
//                }else{
//                    PharmacyNoData.setVisibility(View.GONE);
//                    PharmacyDepartment.setVisibility(View.VISIBLE);
//                    for(DataSnapshot snapshot1: snapshot.getChildren() ){
//                        TeacherData data=snapshot1.getValue(TeacherData.class);
//                        list2.add(data);
//                    }
//                    PharmacyDepartment.setHasFixedSize(true);
//                    PharmacyDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
//                    Adapter=new TeacherAdpter(list2,UpdateFaculty.this);
//                    PharmacyDepartment.setAdapter(Adapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void CSEDepartment() {
//        reference=reference.child("CSE");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list3=new ArrayList<>();
//                if (!snapshot.exists()){
//                    CSENoData.setVisibility(View.VISIBLE);
//                    CSEDepartment.setVisibility(View.GONE);
//                }else{
//                    CSENoData.setVisibility(View.GONE);
//                    CSEDepartment.setVisibility(View.VISIBLE);
//                    for(DataSnapshot snapshot1: snapshot.getChildren() ){
//                        TeacherData data=snapshot1.getValue(TeacherData.class);
//                        list3.add(data);
//                    }
//                    CSEDepartment.setHasFixedSize(true);
//                    CSEDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
//                    Adapter=new TeacherAdpter(list3,UpdateFaculty.this);
//                    CSEDepartment.setAdapter(Adapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(UpdateFaculty.this, error.getMessage(),  Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}