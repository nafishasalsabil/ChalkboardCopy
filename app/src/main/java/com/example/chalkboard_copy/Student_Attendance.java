package com.example.chalkboard_copy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Student_Attendance extends AppCompatActivity {
    FloatingActionButton plus;
    RecyclerView recyclerView;
    StudentAdapter studentAdapter;
    RecyclerView.LayoutManager layoutManager;
    EditText id;
    EditText name;
    private ArrayList<StudentItems> studentItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__attendance);
        plus = findViewById(R.id.addstudent);
        plus.setOnClickListener(v->showDialog());
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
     /*   recyclerView.setLayoutManager(layoutManager);
        studentAdapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(studentAdapter);
       */// studentAdapter.setOnItemClickListener(position -> changeStatus(position));
    }

  /*  private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if(status.equals("P"))
        {
            status = "A";
        }
        else
        {
            status = "P";
        }
        studentItems.get(position).setStatus(status);
        studentAdapter.notifyItemChanged(position);
    }*/

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.studentlayout, null);
        builder.setView(view);
        AlertDialog dialog =  builder.create();
        dialog.show();

        id = view.findViewById(R.id.studentid);
        name = view.findViewById(R.id.studentname);

        Button cancel = view.findViewById(R.id.cancel);
        Button add = view.findViewById(R.id.add);

        cancel.setOnClickListener(v->dialog.dismiss());
        add.setOnClickListener(v-> {
            addclass();
            dialog.dismiss();


        });

    }
    private void addclass() {
        String ids = id.getText().toString();
        String names = name.getText().toString();
     //   studentItems.add(new StudentItems(ids,names));
      //  studentAdapter.notifyDataSetChanged();
    }

}
