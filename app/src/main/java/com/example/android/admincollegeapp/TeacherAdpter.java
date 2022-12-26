package com.example.android.admincollegeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TeacherAdpter extends RecyclerView.Adapter<TeacherAdpter.TeacherViewAdpter> {
    private List<TeacherData> list;
    private Context context;

    public TeacherAdpter(List<TeacherData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherViewAdpter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.facultyitem,parent,false);
        return new TeacherViewAdpter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewAdpter holder, int position) {
        TeacherData Item=list.get(position);
        holder.name.setText(Item.getName());
        holder.email.setText(Item.getEmail());
        holder.phone.setText(Item.getPhone());
        try {
            Picasso.get().load(Item.getImage()).into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Update Teacher", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherViewAdpter extends RecyclerView.ViewHolder {
        private TextView name,email,phone;
        private Button update;
        private ImageView image;
         public TeacherViewAdpter(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.TecaherName);
             email=itemView.findViewById(R.id.TeacherEmail);
             phone=itemView.findViewById(R.id.TecaherPhone);
             update=itemView.findViewById(R.id.teacherUpdate);
             image=itemView.findViewById(R.id.TeacherImage);

        }
    }
}
