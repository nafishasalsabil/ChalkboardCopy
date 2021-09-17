package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPerformanceHT extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    String clicked_courseTitle = "";
    String clicked_course_section = "";
    private RecyclerView recyclerView;
    private ClassPerformanceHTAdapter classPerformanceAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<PerformanceClass> performanceClassList = new ArrayList<>();
    List<QuizNameClass> performanceClassList1 = new ArrayList<>();
    List<PerformanceClass> performanceClassList3 = new ArrayList<>();
    List<StudentItems> student = new ArrayList<>();
    int j=0;
    Toolbar toolbar_cp;
    List<QuizNameClass> quizNameClassList = new ArrayList<>();
    int i=0;
    List<PerformanceClass> list = new ArrayList<>();
    List<PerformanceClass> list2 = new ArrayList<>();
    List<PerformanceClass> list3 = new ArrayList<>();
    double attendance =0;
    double d=0;
    int res = 0;
    int listSize = 0;
    CollectionReference collectionReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_performance_h_t);
        recyclerView = findViewById(R.id.class_performance_recycler_view_ht);
        Intent intent = getIntent();
        clicked_courseTitle = intent.getStringExtra("title");
        clicked_course_section = intent.getStringExtra("section");
        toolbar_cp = findViewById(R.id.toolbar_cp_ht);
        setSupportActionBar(toolbar_cp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_cp.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_cp.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CollectionReference collectionReference = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle)
                .collection("Batches").document(clicked_course_section)
                .collection("Class_Performance");
        collectionReference.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //   list.clear();
                List<PerformanceClass> documentData = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                classPerformanceAdapter = new ClassPerformanceHTAdapter(getApplicationContext(), performanceClassList);
                recyclerView.setAdapter(classPerformanceAdapter);
                performanceClassList.addAll(documentData);
                classPerformanceAdapter.setTitle(clicked_courseTitle);
                classPerformanceAdapter.setSection(clicked_course_section);
                //   classPerformanceAdapter.setListSize(student.size());
                classPerformanceAdapter.notifyDataSetChanged();

            }
        });
        collectionReference1 = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle)
                .collection("Batches").document(clicked_course_section)
                .collection("Class_Performance");
        collectionReference1.orderBy("id").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                list.clear();
                List<PerformanceClass> documentData = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                list.addAll(documentData);
                for(int i=0;i<list.size();i++)
                {
                    //   System.out.println(list.get(i).getId());
                    CollectionReference collectionReference2 = collectionReference1
                            .document(Integer.toString(list.get(i).getId())).collection("quizes");

                    collectionReference2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            double  g =0;
                            list3.clear();
                            List<PerformanceClass> documentData1 = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                            list3.addAll(documentData1);
                            System.out.println(list3.size());

                            for(PerformanceClass performanceClass : documentData1) {
                                list2.add(performanceClass);


//                                System.out.println(list2);

                                System.out.println("SIZE " + list2.size());
                                Log.d("check2",list2.size()+"");
                                Log.d("check3",list3.size()+"");



                                // g = 0;
                                double a =(double) performanceClass.getTotal();
                                Log.d("checkA",a+"");
                                double e =(double) performanceClass.getMarks();
                                Log.d("checkE",e+"");

                                if (a > 0 )
                                {
                                    g = g +( (double) (e /a) * 100);
                                    Log.d("checkG",g+"");

                                    System.out.println(g);
                                }
                                if (list2.size() == list3.size()) {
                                    g = g / list3.size();

                                    DecimalFormat df = new DecimalFormat("#.##");
                                    g = Double.valueOf(df.format(g));
                                    DocumentReference documentReference = collectionReference1
                                            .document(Integer.toString(performanceClass.getId()))
                                            .collection("Marks").document("Coverted_to_100");
                                    Map<String, Object> user1 = new HashMap<>();
                                    user1.put("percentage", g);
                                    documentReference.set(user1);

                                    System.out.println(g);
                                    Log.d("checkG",g+"");

                                    list2.clear();
                                }
                                  /*  g = g / 3.00;

                                    DecimalFormat df = new DecimalFormat("#.##");
                                    g = Double.valueOf(df.format(g));
                                    DocumentReference documentReference = collectionReference1
                                            .document(Integer.toString(performanceClass.getId()))
                                            .collection("quizes").document("Coverted_to_20");
                                    Map<String, Object> user1 = new HashMap<>();
                                    user1.put("converted_quiz", g);
                                    documentReference.set(user1);

                                    System.out.println(g);
*/



                              /*  for (int h = 0; h < list2.size(); h++) {
                                    //  System.out.println(list2.get(h).getId() + " " + list2.get(h).getQuiz_name());
                                    int a = list2.get(h).getTotal();
                                    int e = list2.get(h).getMarks();
                                    g = g + (e * 20) / a;
                                    System.out.println(g);
                                    //    list2.clear();
                                    if (h == list2.size() - 1) {
                                        g = g / 3.00;

                                        DecimalFormat df = new DecimalFormat("#.##");
                                        g = Double.valueOf(df.format(g));
                                        DocumentReference documentReference = collectionReference1
                                                .document(Integer.toString(list.get(h).getId()))
                                                .collection("quizes").document("Coverted_to_20");
                                        Map<String, Object> user1 = new HashMap<>();
                                        user1.put("converted_quiz", g);
                                        documentReference.set(user1);

                                        System.out.println(g);

                                    }


                                }*/
                            }


                        }

                    });

                           /* documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                                    list2.add(performanceClass);
                                    for(int i=0;i<list2.size();i++)
                                    {
                                        System.out.println(list2.get(i).getQuiz_name());

                                    }

                                }
                            });*/

                }

            }


        });


       /* int[] result = new int[4];
        CollectionReference quizcolllection1 = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle)
                .collection("Sections").document(clicked_course_section).collection("Students");
        quizcolllection1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                student.addAll(documentData);
                for(int k=0;k<student.size();k++)
                {
                    String b= Integer.toString(student.get(k).getId());
                  //  System.out.println(student.get(k).getId());
                    CollectionReference quizcolllection = firestore.collection("users").document(userID)
                            .collection("Courses").document(clicked_courseTitle)
                            .collection("Sections").document(clicked_course_section).collection("Quizes");
                    quizcolllection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            quizNameClassList.clear();
                            List<QuizNameClass> documentData = queryDocumentSnapshots.toObjects(QuizNameClass.class);
                            quizNameClassList.addAll(documentData);
                            Log.d("checkLength",quizNameClassList.size()+"");
                            int f =quizNameClassList.size();
                            for( i=0;i<quizNameClassList.size();i++)
                            {

                              //  System.out.println(quizNameClassList.get(i).getQuiz());
                                int c = quizNameClassList.get(i).getQuiz_total_marks();
                                System.out.println(c +"total marks");
                                DocumentReference documentReference = firestore.collection("users").document(userID)
                                    .collection("Courses").document(clicked_courseTitle)
                                    .collection("Sections").document(clicked_course_section)
                                        .collection("Class_Performance").document(b)
                                        .collection(quizNameClassList.get(i).getQuiz()).document("marks");
                                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                       PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                                      //  System.out.println(performanceClass.getMarks());
                                        int m = performanceClass.getMarks();
                                        System.out.println(performanceClass.getId());
                                        System.out.println(b);

                                        if(i<quizNameClassList.size())
                                        {
                                            res = res+((m * 20) / c);
                                            Log.d("checkArray", res + "");

                                        }
                                        else {
                                            float res1 = res/3;
                                            Log.d("checkArrayFinal", res1 + "");
                                            res=0;
                                            Log.d("checkArrayEmpty", res + "");

                                        }
                                         *//*System.out.println(b);
                                        System.out.println(performanceClass.getId());
                                        *//*

         *//*if(Integer.toString(performanceClass.getId()).equals(b)) {


                                            if (o < f) {
                                                result[o] = (m * 20) / c;
                                                o++;
                                            }
                                            Log.d("checkArray", result + "");
                                        }*//*
                                    }

                                });


                            }


                        }
                    });
                }


            }
        });

*/

      /*  CollectionReference quizcolllection1 = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle)
                .collection("Sections").document(clicked_course_section).collection("Quizes");
        quizcolllection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<QuizNameClass> documentData = queryDocumentSnapshots.toObjects(QuizNameClass.class);
                quizNameClassList.addAll(documentData);
                Log.d("checkLength",quizNameClassList.size()+"");

            }
        });
*/
      /*  CollectionReference collectionReference1 = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle)
                .collection("Sections").document(clicked_course_section)
                .collection("Class_Performance");
        collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<PerformanceClass> performanceClassList2 = queryDocumentSnapshots.toObjects(PerformanceClass.class);


                   DocumentReference documentReference = firestore.collection("users").document(userID)
                              .collection("Courses").document(clicked_courseTitle)
                              .collection("Sections").document(clicked_course_section)
                              .collection("Class_Performance").document(Integer.toString(performanceClassList2.));
                      documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                          @Override
                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                              QuizNameClass performanceClass1 = documentSnapshot.toObject(QuizNameClass.class);
                              performanceClassList1.add(performanceClass1);
                              System.out.println(performanceClass1);

                              *//*for(int i=0;i<performanceClassList1.size();i++)
                              {
                                  Log.d("checkQuiz",performanceClassList1.get(i).toString()+"");

                              }*//*
                          }
                      });
                }

            }
        });

*/

        /*documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                if(performanceClass.getCount()==listSize)
                {
                   *//* System.out.println(performanceClassList.get(position).getId());
                    System.out.println(10);*//*
                    attendance = 10;
                    Log.d("checkAtt",attendance+"");
                    DocumentReference documentReference1 =  firestore.collection("users").document(userID)
                            .collection("Courses").document(title)
                            .collection("Sections").document(section)
                            .collection("Class_Performance").document(holder.roll.getText().toString())
                            .collection("Backup").document("Attendance_Marks");
                    Map<String, Object> user1 = new HashMap<>();
                    user1.put("attendance", attendance);
                    documentReference.set(user1, SetOptions.merge());


                }
                else{
                    if(performanceClass.getCount()<listSize && performanceClass.getCount()>((listSize*60)/100))
                    {
                        attendance =7;
                        Log.d("checkAtt",attendance+"");
                        DocumentReference documentReference1 =  firestore.collection("users").document(userID)
                                .collection("Courses").document(title)
                                .collection("Sections").document(section)
                                .collection("Class_Performance").document(holder.roll.getText().toString())
                                .collection("Backup").document("Attendance_Marks");
                        Map<String, Object> user1 = new HashMap<>();
                        user1.put("attendance", attendance);
                        documentReference.set(user1, SetOptions.merge());

                    }
                    else
                    {
                        attendance =4;
                        Log.d("checkAtt",attendance+"");
                        DocumentReference documentReference1 =  firestore.collection("users").document(userID)
                                .collection("Courses").document(title)
                                .collection("Sections").document(section)
                                .collection("Class_Performance").document(holder.roll.getText().toString())
                                .collection("Backup").document("Attendance_Marks");
                        Map<String, Object> user1 = new HashMap<>();
                        user1.put("attendance", attendance);
                        documentReference.set(user1, SetOptions.merge());

                    }
                }

           *//*     Log.d("checkAtt",attendance+"");
                DocumentReference documentReference1 = documentReference
                        .collection("Backup").document("Attendance_Marks");
                Map<String, Object> user1 = new HashMap<>();
                user1.put("attendance", attendance);
                documentReference.set(user1);
*//*

            }
        });

*/
    }
}