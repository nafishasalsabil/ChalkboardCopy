package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
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
        getSupportActionBar().setTitle("Class Performance");
        toolbar_cp.setTitleTextColor(Color.BLACK);
        toolbar_cp.setNavigationOnClickListener(new View.OnClickListener() {
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


                    CollectionReference collectionReference =  firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(clicked_courseTitle)
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
                    collectionReference1 =  firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(clicked_courseTitle)
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

                                        }


                                    }

                                });



                            }

                        }


                    });

                }
                else if(!(status.equals("Professional teacher / Home tutor"))){

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

                                        }


                                    }

                                });



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