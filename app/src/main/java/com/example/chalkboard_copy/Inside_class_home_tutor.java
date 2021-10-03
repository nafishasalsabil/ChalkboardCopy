package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Inside_class_home_tutor extends AppCompatActivity {
    CardView attendance,materials,results,student_info;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    Toolbar toolbar_inside_class;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView profilepic;
    public TextView username;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public String u = "";
    FirebaseFirestore firestore;
    String userID;
    String courseTitle="";
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private NavController navController;
    public static String title  = "";
    public static String section  = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_class_home_tutor);
        attendance = findViewById(R.id.attendance_cardview_hometutor);
        materials = findViewById(R.id.materials_cardview_hometutor);
        results = findViewById(R.id.results_cardview_hometutor);
        student_info = findViewById(R.id.studentinfo_cardview_hometutor);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //   signout = findViewById(R.id.signout);
        toolbar_inside_class = findViewById(R.id.inside_class_toolbar_hometutor);
        drawerLayout = findViewById(R.id.inside_class_dlayout);
        navigationView = findViewById(R.id.navigationView);
//        navController = Navigation.findNavController(this, R.id.main);
        //firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID =  firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference = firestore.collection("users").document(userID);
//

        setSupportActionBar(toolbar_inside_class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        section = intent.getStringExtra("section");
        title = intent.getStringExtra("title");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        toolbar_inside_class.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(section);
        toolbar_inside_class.setTitleTextColor(Color.BLACK);
        toolbar_inside_class.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Batch_inside_courses_home_tutor.class);
                intent.putExtra("title",title);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StudentListHometutor.class);
                intent.putExtra("section",section);
                intent.putExtra("title",title);
                startActivity(intent);
                finish();
            }
        });
        materials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Materials_hometutor.class);
                intent.putExtra("section",section);
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });
        results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ResultsHomeTutor.class);
                intent.putExtra("section",section);
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });
        student_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),StudentInfoHomeTutor.class);
                intent.putExtra("section",section);
                intent.putExtra("title",title);
                startActivity(intent);

            }
        });



    }
}