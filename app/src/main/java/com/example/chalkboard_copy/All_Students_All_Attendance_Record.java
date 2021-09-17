package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class All_Students_All_Attendance_Record extends AppCompatActivity {
    private RecyclerView recyclerView;
    private All_Students_All_Attandance_RecordAdapter all_students_all_attendance_recordAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<StudentItems> studentItems = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    private CollectionReference collectionReference;
  Toolbar asar_toolbar;
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    public final String BOTH_PROFESSION="Professional teacher / Home tutor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__students__all__attendance__record);
        Intent intent = getIntent();
        String lec = intent.getStringExtra("lecture");
        String title = intent.getStringExtra("title");
        String section = intent.getStringExtra("section");
        asar_toolbar = findViewById(R.id.toolbar_asar);
        setSupportActionBar(asar_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        asar_toolbar.setNavigationIcon(R.drawable.ic_back);
        recyclerView = (RecyclerView)findViewById(R.id.all_attendance_record_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);




        firestore = FirebaseFirestore.getInstance();
        userID =  firebaseAuth.getCurrentUser().getUid();
        System.out.println(userID);
        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();
                System.out.println(status+"___________Loading?");
                System.out.println(status+" Check");
                if(status.equals("Professional teacher / Home tutor")){

                    System.out.println("Working??????????????????????????");

                    collectionReference = firestore.collection("users").document(userID).collection("All Files").document(PROF)
                            .collection("Courses").document(title).collection("Sections")
                            .document(section)
                            .collection("Attendance").document(lec).collection("Status");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            studentItems.clear();
                            List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                            all_students_all_attendance_recordAdapter = new All_Students_All_Attandance_RecordAdapter(getApplicationContext(), studentItems);
                            all_students_all_attendance_recordAdapter.setLectureName(lec);
                            recyclerView.setAdapter(all_students_all_attendance_recordAdapter);
                            all_students_all_attendance_recordAdapter.setTitle(title);
                            all_students_all_attendance_recordAdapter.setSection(section);
                            studentItems.addAll(documentData);
                            all_students_all_attendance_recordAdapter.notifyDataSetChanged();

                            //   studentItems.add(new StudentItems(id1, name1, ""));

                        }
                    });

                }
                else if(!(status.equals("Professional teacher / Home tutor"))){

                    System.out.println("Not Working??????????????????????????");
                    collectionReference = firestore.collection("users").document(userID)
                            .collection("Courses").document(title).collection("Sections")
                            .document(section)
                            .collection("Attendance").document(lec).collection("Status");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                            all_students_all_attendance_recordAdapter = new All_Students_All_Attandance_RecordAdapter(getApplicationContext(), studentItems);
                            all_students_all_attendance_recordAdapter.setLectureName(lec);
                            recyclerView.setAdapter(all_students_all_attendance_recordAdapter);
                            all_students_all_attendance_recordAdapter.setTitle(title);
                            all_students_all_attendance_recordAdapter.setSection(section);
                            studentItems.addAll(documentData);
                            all_students_all_attendance_recordAdapter.notifyDataSetChanged();

                            //   studentItems.add(new StudentItems(id1, name1, ""));

                        }
                    });


                }


            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });















        asar_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}