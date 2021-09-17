package com.example.chalkboard_copy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener {
    Button prof,tutor,both;
    private FirebaseAuth firebaseAuth;
    //FirebaseDatabase firebaseDatabase;
   // DatabaseReference databaseReference;
    FirebaseFirestore firestore;
    String userID;
    SharedPreferences sharedPreferences;
Boolean select = false;
  public   String proff = "Professional teacher";
    public String home = "Home tutor";
    public String both_selected = "Professional teacher / Home tutor";
    Boolean normal_signin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        Intent intent = getIntent();
        normal_signin = getIntent().getExtras().getBoolean("Normal_signin");

        prof = findViewById(R.id.proft);
        tutor = findViewById(R.id.tutor);
        both = findViewById(R.id.both_prof_ht);
        both.setOnClickListener(this);
        prof.setOnClickListener(this);
        tutor.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
       firestore = FirebaseFirestore.getInstance();


        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courses = " ";
                select = true;

                sharedPreferences = getSharedPreferences("selected", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putBoolean("lockedState", select);
                editor.apply();

                userID =  firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firestore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("choice",proff);
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(),Features.class);
                        intent.putExtra("Normal_signin",normal_signin);
                        startActivity(intent);
                       // startActivity(new Intent(getApplicationContext(), Features.class));


                    }
                });


                //    firebaseDatabase = FirebaseDatabase.getInstance();
             //   databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getUid()).child("choice");
             /*   databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            user u = new user(prof,courses);
                            databaseReference.setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(getApplicationContext(),Features.class);
                                    startActivity(intent);

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
                        }
        });
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courses = " ";

                userID =  firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firestore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("choice",home);

                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), MainActivity_HomeTutor.class));


                    }
                });

            }
        });

        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courses = " ";

                userID =  firebaseAuth.getCurrentUser().getUid();
                DocumentReference documentReference = firestore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("choice",both_selected);

                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(getApplicationContext(), Features.class));


                    }
                });

            }
        });


    }

    @Override
    public void onClick(View v) {
         //   databaseReference.push()

    }


}
