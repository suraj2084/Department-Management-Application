package com.example.android.admincollegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DepartmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView bcacourse;
        TextView bsccourse;
        TextView mcacourse;
        setContentView(R.layout.activity_department);
        bcacourse=findViewById(R.id.BCAcourse);
        bsccourse=findViewById(R.id.BSCcourse);
        mcacourse=findViewById(R.id.MCAcourse);
        bcacourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DepartmentActivity.this, BCACourse.class));
            }
        });
        bsccourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DepartmentActivity.this, BSCCourse.class));
            }
        });
        mcacourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DepartmentActivity.this, MCACourse.class));
            }
        });
    }
}