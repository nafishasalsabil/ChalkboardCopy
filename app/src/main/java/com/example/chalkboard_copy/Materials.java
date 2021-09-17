package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Materials extends AppCompatActivity {

    Toolbar toolbar_materials;
    RelativeLayout pdf_layout,tutorials_layout,exam_ques_layout,my_notes_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);
        Intent intent  = getIntent();
        String title  =intent.getStringExtra("title");
        String sec = intent.getStringExtra("section");
        toolbar_materials = findViewById(R.id.toolbar_materials);
        setSupportActionBar(toolbar_materials);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Materials");
        toolbar_materials.setTitleTextColor(Color.BLACK);
        toolbar_materials.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        toolbar_materials.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pdf_layout = findViewById(R.id.pdf_layout);
        tutorials_layout = findViewById(R.id.tutorilas_layout);
        exam_ques_layout = findViewById(R.id.exam_questions_layout);
        my_notes_layout = findViewById(R.id.my_notes_layout);


        pdf_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),PDFs.class);
                intent1.putExtra("section",sec);
                intent1.putExtra("title",title);
                startActivity(intent1);
            }
        });
        exam_ques_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),Exam_Questions.class);
                intent1.putExtra("section",sec);
                intent1.putExtra("title",title);
                startActivity(intent1);

            }
        });
        my_notes_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),MyNotes.class);
                intent1.putExtra("section",sec);
                intent1.putExtra("title",title);
                startActivity(intent1);

            }
        });

        tutorials_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),Tutorials.class);
                intent1.putExtra("section",sec);
                intent1.putExtra("title",title);
                startActivity(intent1);

            }
        });
    }
}
