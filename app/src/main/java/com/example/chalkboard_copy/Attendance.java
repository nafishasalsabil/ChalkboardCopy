package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {

    FloatingActionButton plus;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Classitem> classitems = new ArrayList<>();
    EditText classedit;
    EditText courseedit;
    Boolean isScrolling = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        plus = findViewById(R.id.addclass);
        plus.setOnClickListener(v -> showDialog());

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        classAdapter = new ClassAdapter(this,classitems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoitemactivity(position));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling =true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void gotoitemactivity(int position) {
        Intent intent = new Intent(this,Student_Attendance.class);
       // intent.putExtra("Classname",classitems.get(position).getClassname());
      //  intent.putExtra("Course",classitems.get(position).getCourse());
        startActivity(intent);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.classlayout, null);
        builder.setView(view);
        AlertDialog dialog =  builder.create();
        dialog.show();

        classedit = view.findViewById(R.id.classname);
        courseedit = view.findViewById(R.id.coursename);

        Button cancel = view.findViewById(R.id.cancel);
        Button add = view.findViewById(R.id.add);

        cancel.setOnClickListener(v->dialog.dismiss());
        add.setOnClickListener(v-> {
            addclass();
            dialog.dismiss();


        });
    }

    private void addclass() {
        String classname = classedit.getText().toString();
        String course = courseedit.getText().toString();
        classitems.add(new Classitem(classname,course));
        classAdapter.notifyDataSetChanged();
    }


}
