package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class ResultsHomeTutor extends AppCompatActivity {
    CardView quiz_marks,class_performance,cg;
    Toolbar results_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_home_tutor);
        quiz_marks = findViewById(R.id.quiz_marks_cardview_ht);
        class_performance = findViewById(R.id.performance_ht);
        cg = findViewById(R.id.cgpa_ht);
        results_toolbar = findViewById(R.id.toolbar_results_ht);
        setSupportActionBar(results_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        results_toolbar.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setTitle("Results");
        results_toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setElevation(0);
        results_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent i = getIntent();
        String sec =    i.getStringExtra("section");
        String title =  i.getStringExtra("title");
        quiz_marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MarksHomeTutor.class);
                intent.putExtra("section",sec);
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });
        class_performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ClassPerformanceHT.class);
                intent.putExtra("section",sec);
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });
        cg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MonthlyIncomeHomeTutor.class);
                intent.putExtra("section",sec);
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });
    }
}