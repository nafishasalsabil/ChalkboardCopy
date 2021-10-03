package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentInfoHomeTutor extends AppCompatActivity {
    ImageView info;
    RecyclerView info_recycler_view;
    private StudentInfoHomeTutorAdapter studentInfoAdapter;
    private ClassAdapter classAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<StudentItems> studentItems = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    private DocumentReference documentReference, documentReference2, studentsdocument, documentReference3;
    private CollectionReference collectionReference, studentcollection;
    TextView t1;
    Toolbar toolbar_info;
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_home_tutor);
        info_recycler_view = findViewById(R.id.student_info_recyclerview_ht);
        t1 = findViewById(R.id.no_info_text);

        toolbar_info = findViewById(R.id.toolbar_s_info_ht);
        setSupportActionBar(toolbar_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_info.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Student Info");
        toolbar_info.setTitleTextColor(Color.BLACK);
        toolbar_info.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String section = intent.getStringExtra("section");
        String title = intent.getStringExtra("title");
        info_recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        info_recycler_view.setLayoutManager(layoutManager);


        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){
                    DocumentReference retrievesection =firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(title);

                    retrievesection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String k = documentSnapshot.getString("section");
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                    studentcollection =firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(title).collection("Batches")
                            .document(section)
                            .collection("Students");
                    studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                            studentInfoAdapter = new StudentInfoHomeTutorAdapter(getApplicationContext(), studentItems);
                            info_recycler_view.setAdapter(studentInfoAdapter);
                            studentItems.addAll(documentData);
                            studentInfoAdapter.setTitle(title);
                            studentInfoAdapter.setSection(section);
                            studentInfoAdapter.notifyDataSetChanged();
                            if (studentInfoAdapter.getItemCount() == 0) {
                                t1.setVisibility(View.VISIBLE);
                            } else {
                                t1.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < studentItems.size(); i++) {
                                System.out.println(studentItems.get(i).toString());
                            }



                        }
                    });
                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    DocumentReference retrievesection = firestore.collection("users")
                            .document(userID).collection("Courses").document(title);

                    retrievesection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String k = documentSnapshot.getString("section");
                            }
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                    studentcollection = firestore.collection("users").document(userID)
                            .collection("Courses").document(title).collection("Batches")
                            .document(section)
                            .collection("Students");
                    studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                            studentInfoAdapter = new StudentInfoHomeTutorAdapter(getApplicationContext(), studentItems);
                            info_recycler_view.setAdapter(studentInfoAdapter);
                            studentItems.addAll(documentData);
                            studentInfoAdapter.setTitle(title);
                            studentInfoAdapter.setSection(section);
                            studentInfoAdapter.notifyDataSetChanged();
                            if (studentInfoAdapter.getItemCount() == 0) {
                                t1.setVisibility(View.VISIBLE);
                            } else {
                                t1.setVisibility(View.GONE);
                            }
                            for (int i = 0; i < studentItems.size(); i++) {
                                System.out.println(studentItems.get(i).toString());
                            }



                        }
                    });

                }




            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });






    }
}