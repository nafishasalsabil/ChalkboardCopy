package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

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

public class CGPACalculator extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    String clicked_courseTitle = "";
    String clicked_course_section = "";
    private RecyclerView recyclerView;
    private CgpaAdapter cgpaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar_cg;
    List<PerformanceClass> performanceClassList = new ArrayList<>();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_g_p_a_calculator);
        Intent intent = getIntent();
        clicked_courseTitle = intent.getStringExtra("title");
        clicked_course_section = intent.getStringExtra("section");
        System.out.println(clicked_courseTitle);
        System.out.println(clicked_course_section);
        recyclerView = (RecyclerView) findViewById(R.id.cgpa_recycler_view);
        toolbar_cg = findViewById(R.id.toolbar_cg);
        setSupportActionBar(toolbar_cg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_cg.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);

        getSupportActionBar().setTitle("CGPA Calculator");
        toolbar_cg.setTitleTextColor(Color.BLACK);
        toolbar_cg.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){

                    CollectionReference collectionReference = firestore.collection("users").document(userID)
                            .collection("All Files").document(PROF).collection("Courses")
                            .document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                            .collection("Class_Performance");
                    collectionReference.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //   list.clear();
                            List<PerformanceClass> documentData = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                            cgpaAdapter = new CgpaAdapter(getApplicationContext(), performanceClassList);
                            recyclerView.setAdapter(cgpaAdapter);
                            performanceClassList.addAll(documentData);
                            for(int i=0;i<performanceClassList.size();i++)
                            {
                                System.out.println(performanceClassList.get(i));
                            }
                            cgpaAdapter.setTitle(clicked_courseTitle);
                            cgpaAdapter.setSec(clicked_course_section);
                            //   classPerformanceAdapter.setListSize(student.size());
                            cgpaAdapter.notifyDataSetChanged();

                        }
                    });




                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    CollectionReference collectionReference = firestore.collection("users").document(userID)
                            .collection("Courses").document(clicked_courseTitle)
                            .collection("Sections").document(clicked_course_section)
                            .collection("Class_Performance");
                    collectionReference.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //   list.clear();
                            List<PerformanceClass> documentData = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                            cgpaAdapter = new CgpaAdapter(getApplicationContext(), performanceClassList);
                            recyclerView.setAdapter(cgpaAdapter);
                            performanceClassList.addAll(documentData);
                            for(int i=0;i<performanceClassList.size();i++)
                            {
                                System.out.println(performanceClassList.get(i));
                            }
                            cgpaAdapter.setTitle(clicked_courseTitle);
                            cgpaAdapter.setSec(clicked_course_section);
                            //   classPerformanceAdapter.setListSize(student.size());
                            cgpaAdapter.notifyDataSetChanged();

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