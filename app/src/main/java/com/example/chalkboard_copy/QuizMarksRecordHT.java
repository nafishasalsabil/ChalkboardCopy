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

public class QuizMarksRecordHT extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    private QuizMarksRecordHTAdapter quizMarksRecordAdapter;
    private ClassAdapter classAdapter;
    private RecyclerView.LayoutManager layoutManager;
    CollectionReference studentcollection;
    List<QuizMarksClass> studentItems1 = new ArrayList<>();
    List<QuizNameClass> studentItems2 = new ArrayList<>();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    Toolbar toolbar_record;
    int total_marks = 0;
    RecyclerView quiz_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_marks_record_h_t);
        quiz_rv = findViewById(R.id.quiz_record_recyclerview_ht);
        toolbar_record = findViewById(R.id.toolbar_quiz_record_ht);
        setSupportActionBar(toolbar_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_record.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);

        toolbar_record.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String title =  intent.getStringExtra("title");
        String section =  intent.getStringExtra("section");
        String quiz = intent.getStringExtra("quiz");
        System.out.println(quiz);
        System.out.println(title);
        System.out.println(section);
        getSupportActionBar().setTitle(quiz);
        toolbar_record.setTitleTextColor(Color.BLACK);
        quiz_rv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        quiz_rv.setLayoutManager(layoutManager);
        quizMarksRecordAdapter = new QuizMarksRecordHTAdapter(getApplicationContext(), studentItems1);
        quiz_rv.setAdapter(quizMarksRecordAdapter);
        //   studentItems1.addAll(documentData);
        quizMarksRecordAdapter.notifyDataSetChanged();


        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){


                    studentcollection = firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(title).collection("Batches")
                            .document(section)
                            .collection("Students");


                    CollectionReference collectionReference =   firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(title).collection("Batches")
                            .document(section)
                            .collection("Quizes").document(quiz).collection("Students");
                    collectionReference.orderBy("id").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            studentItems1.clear();
                            List<QuizMarksClass> documentData = queryDocumentSnapshots.toObjects(QuizMarksClass.class);
                            quiz_rv.setAdapter(quizMarksRecordAdapter);
                            quizMarksRecordAdapter.setTitle(title);
                            quizMarksRecordAdapter.setSection(section);
                            quizMarksRecordAdapter.setQuiz(quiz);
                            studentItems1.addAll(documentData);
                            quizMarksRecordAdapter.notifyDataSetChanged();
                        }
                    });

                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    studentcollection = firestore.collection("users").document(userID)
                            .collection("Courses").document(title).collection("Batches")
                            .document(section)
                            .collection("Students");


                    CollectionReference collectionReference =   firestore.collection("users").document(userID)
                            .collection("Courses").document(title).collection("Batches")
                            .document(section)
                            .collection("Quizes").document(quiz).collection("Students");
                    collectionReference.orderBy("id").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            studentItems1.clear();
                            List<QuizMarksClass> documentData = queryDocumentSnapshots.toObjects(QuizMarksClass.class);
                            quiz_rv.setAdapter(quizMarksRecordAdapter);
                            quizMarksRecordAdapter.setTitle(title);
                            quizMarksRecordAdapter.setSection(section);
                            quizMarksRecordAdapter.setQuiz(quiz);
                            studentItems1.addAll(documentData);
                            quizMarksRecordAdapter.notifyDataSetChanged();
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