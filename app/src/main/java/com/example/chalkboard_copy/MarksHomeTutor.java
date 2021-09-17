package com.example.chalkboard_copy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarksHomeTutor extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    public static String sec = "";
    public static String title_course = "";
    FloatingActionButton floatingActionButton_quiz;
    QuizNameClass quizNameClass = new QuizNameClass();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    RecyclerView quiz_recylerview ;
    private QuizMarksHTAdapter quizMarksAdapter;
    private RecyclerView.LayoutManager layoutManager;
    CollectionReference quizcolllection;
    List<QuizNameClass> quizItems = new ArrayList<>();
    TextView t1;
    Toolbar toolbar_quiz;
    List<QuizMarksClass> studentItems1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_home_tutor);
        Intent intent = getIntent();
        sec =  intent.getStringExtra("section");
        title_course =  intent.getStringExtra("title");
        floatingActionButton_quiz = findViewById(R.id.add_quiz_fab_ht);
        t1 = findViewById(R.id.nqt1);
        toolbar_quiz = findViewById(R.id.toolbar_quiz_ht);
        setSupportActionBar(toolbar_quiz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_quiz.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        toolbar_quiz.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        quiz_recylerview = (RecyclerView) findViewById(R.id.quiz_recyclerview);
        quiz_recylerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        quiz_recylerview.setLayoutManager(layoutManager);
        quizcolllection = firestore.collection("users").document(userID)
                .collection("Courses").document(title_course)
                .collection("Batches").document(sec).collection("Quizes");
        quizcolllection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<QuizNameClass> documentData = queryDocumentSnapshots.toObjects(QuizNameClass.class);
                quizMarksAdapter = new QuizMarksHTAdapter(getApplicationContext(), quizItems);
                quiz_recylerview.setAdapter(quizMarksAdapter);
                quizItems.addAll(documentData);
                quizMarksAdapter.setSec(sec);
                quizMarksAdapter.setTitle(title_course);
                quizMarksAdapter.notifyDataSetChanged();
                t1.setVisibility(View.GONE);
                if(quizMarksAdapter.getItemCount()==0)
                {
                    t1.setVisibility(View.VISIBLE);

                }
            }
        });



        floatingActionButton_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(MarksHomeTutor.this);
                View view = LayoutInflater.from(MarksHomeTutor.this).inflate(R.layout.add_quiz_dialog_box_ht, null);
                alerDialog2.setView(view);
                AlertDialog dialog = alerDialog2.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                Button add_quiz_done = view.findViewById(R.id.done_quiz_add_ht);
                EditText quiz_name =view. findViewById(R.id.quiz_name_edittext_ht);
                TextView quiz_date = view.findViewById(R.id.quiz_date_textview_ht);
                EditText total_marks =view. findViewById(R.id.total_marks_ht);
                final Calendar newCalender = Calendar.getInstance();

                quiz_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog dialog = new DatePickerDialog(MarksHomeTutor.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                                final Calendar newDate = Calendar.getInstance();
                                Calendar newTime = Calendar.getInstance();
                                String date_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                                quiz_date.setText(date_date);
                                // studentItems_object.setLecture_date(date_date);


                            }
                        },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                        dialog.show();


                    }
                });
                add_quiz_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String q_n = quiz_name.getText().toString();
                        String q_d = quizNameClass.getQuiz_date();
                        int q = Integer.parseInt(total_marks.getText().toString());
                        if (TextUtils.isEmpty(q_n)) {
                            quiz_name.setError("Quiz name is required");
                            return;
                        }
                        if (TextUtils.isEmpty(q_d)) {
                            quiz_date.setError("Quiz date is required");
                            return;
                        }
                        if (TextUtils.isEmpty(total_marks.getText().toString())) {
                            total_marks.setError("Quiz marks is required");
                            return;
                        }
                        DocumentReference documentReference = firestore.collection("users").document(userID)
                                .collection("Courses").document(title_course)
                                .collection("Batches").document(sec).collection("Quizes").document(q_n);

                        Map<String, Object> user = new HashMap<>();
                        user.put("quiz", q_n);
                        user.put("quiz_date",q_d);
                        user.put("quiz_total_marks",q);
                        documentReference.set(user);
                        QuizNameClass quizNameClass = new QuizNameClass(q_n,q_d,q);
                        quizMarksAdapter = new QuizMarksHTAdapter(getApplicationContext(), quizItems);
                        quiz_recylerview.setAdapter(quizMarksAdapter);
                        quizItems.add(quizNameClass);
                        quizMarksAdapter.notifyDataSetChanged();




                        dialog.dismiss();
                        CollectionReference studentcollection = firestore.collection("users").document(userID)
                                .collection("Courses").document(title_course).collection("Batches")
                                .document(sec)
                                .collection("Students");


                        studentcollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                {
                                    QuizMarksClass items = documentSnapshot.toObject(QuizMarksClass.class);

                                    studentItems1.add(items);
                                    System.out.println(studentItems1);
                                    DocumentReference documentReference =  firestore.collection("users").document(userID)
                                            .collection("Courses").document(title_course).collection("Batches")
                                            .document(sec)
                                            .collection("Quizes").document(q_n).collection("Students").document(Integer.toString(items.getId()));
                                    System.out.println(items.getId());
                                    Log.d("checkO",Integer.toString(items.getId()));
                                    Log.d("checkO",Integer.toString(studentItems1.size()));

                                    for(int i=0;i<studentItems1.size();i++) {
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("id", items.getId());
                                        user.put("name", items.getName());
                                        user.put("marks", 0);
                                        documentReference.set(user);

                                        DocumentReference documentReference1 = firestore.collection("users").document(userID)
                                                .collection("Courses").document(title_course).collection("Batches")
                                                .document(sec)
                                                .collection("Class_Performance")
                                                .document(Integer.toString(items.getId()))
                                                .collection("quizes").document(q_n);


                                        Map<String, Object> user1 = new HashMap<>();
                                        user1.put("id", items.getId());
                                        user1.put("name", items.getName());
                                        user1.put("marks", 0);
                                        user1.put("total", q);
                                        user1.put("quiz_name",q_n);
                                        documentReference1.set(user1);
                                    }

                                }


                            }
                        });



                    }
                });

            }
        });

    }
}