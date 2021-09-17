package com.example.chalkboard_copy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Attendance_activity_hometutor extends AppCompatActivity {
    private static final int REQUEST_CODE = 5795;
    private static final String TAG = "checked";
    RelativeLayout present, absent, late, layer;
    TextView t1, t2;
    DatePickerDialog datePickerDialog;
    private ListView listView;
    private StudentHomeTutorAdapter studentAdapter;
    private ClassAdapter classAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public boolean x;
    public static View student_view;
    FloatingActionButton attendance_done_fab;
    List<StudentItems> studentItems = new ArrayList<>();
    List<CourseInfo> classitems = new ArrayList<>();
    public static TextView presentcount, absentcount, latecount;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    String title = "";
    Button done;
    public static String clicked_courseTitle_ht = "";
    public static String clicked_course_section_ht = "";
    SharedPreferences sharedPreferences1, sharedPreferences2, sharedPreferences3;
    private DocumentReference documentReference, documentReference2, studentsdocument, documentReference3;
    private CollectionReference collectionReference, studentcollection;
    static int p = 0, a = 0, l = 0;
    Button b;
    public static String detect1 = "";
    public static String detect2 = "";
    private AlertDialog.Builder alerDialog;
    private AlertDialog.Builder alertdialog_for_attendance;
    // Lecture object = new Lecture();
    StudentItems studentItems_object = new StudentItems();
    CourseInfo courseInfo = new CourseInfo();

    Toolbar toolbar_attendance;
    File myExternalFile;
    String myData = "";
    Button fc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_hometutor_activity);
        present = findViewById(R.id.present_layout_ht);
        absent = findViewById(R.id.absent_layout_ht);
        late = findViewById(R.id.late_layout_ht);
        b = findViewById(R.id.b);
        fc = findViewById(R.id.file_choser);
        toolbar_attendance = findViewById(R.id.toolbar_attendance_ht);
        setSupportActionBar(toolbar_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_attendance.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        toolbar_attendance.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        attendance_done_fab = findViewById(R.id.attendance_done_fab_ht);
        //     layer = findViewById(R.id.lec_layer);
        t1 = findViewById(R.id.tv1);
        t2 = findViewById(R.id.tv2);
        //    date = findViewById(R.id.date);
        //     lecture = findViewById(R.id.lecture);
//        lecture.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        presentcount = findViewById(R.id.present_student_count_ht);
        absentcount = findViewById(R.id.absent_student_count_ht);
        latecount = findViewById(R.id.late_student_count_ht);
        listView = (ListView)findViewById(R.id.students_recycler_view_ht);
        student_view = findViewById(R.id.student_view);
        Intent intent = getIntent();
        clicked_courseTitle_ht = intent.getStringExtra("title");
        clicked_course_section_ht = intent.getStringExtra("section");
        System.out.println(clicked_courseTitle_ht);

        //   recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        //   recyclerView.setLayoutManager(layoutManager);
        DocumentReference retrievesection = firestore.collection("users")
                .document(userID).collection("Courses").document(clicked_courseTitle_ht);

        retrievesection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String k = documentSnapshot.getString("section");
                    courseInfo.setSection(k);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


           studentcollection = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle_ht).collection("Batches")
                .document(clicked_course_section_ht)
                .collection("Students");
        studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                studentAdapter = new StudentHomeTutorAdapter(getApplicationContext(), studentItems);
                listView.setAdapter(studentAdapter);
                studentItems.addAll(documentData);
                detect1 = "make_invisible";
                //   studentAdapter.setUi(detect1);
                studentAdapter.setTitle(clicked_courseTitle_ht);
                studentAdapter.setSec(clicked_course_section_ht);
                studentAdapter.notifyDataSetChanged();
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                for (int i = 0; i < studentItems.size(); i++) {
                    System.out.println(studentItems.get(i).toString());
                }

            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(Attendance_activity_hometutor.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showChooser();
            }
        });
        presentcount.setText(String.valueOf(p));
        absentcount.setText(String.valueOf(a));
        latecount.setText(String.valueOf(l));

        userID = firebaseAuth.getCurrentUser().getUid();
        attendance_done_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=0;
                l=0;
                a=0;
                presentcount.setText(String.valueOf(p));
                absentcount.setText(String.valueOf(a));
                latecount.setText(String.valueOf(l));


                Intent intent1 = new Intent(getApplicationContext(), StudentListHometutor.class);
                intent1.putExtra("title",clicked_courseTitle_ht);
                intent1.putExtra("section",clicked_course_section_ht);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent1);

            }
        });


    }
    @SuppressLint("ResourceAsColor")
    public static void statusA(boolean present, boolean late) {
        a = Integer.parseInt(absentcount.getText().toString()) + 1;

        if (present) {
            p = Integer.parseInt(presentcount.getText().toString()) - 1;

        } else {

            l = Integer.parseInt(latecount.getText().toString()) - 1;
        }

        presentcount.setText(String.valueOf(p));
        absentcount.setText(String.valueOf(a));
        latecount.setText(String.valueOf(l));
        //   student_view.setBackgroundResource(R.color.a);


    }

    public static void statusL(boolean present, boolean abs) {
        l = Integer.parseInt(latecount.getText().toString()) + 1;

        if (abs) {
            a = Integer.parseInt(absentcount.getText().toString()) - 1;

        } else {

            p = Integer.parseInt(presentcount.getText().toString()) - 1;
        }

        presentcount.setText(String.valueOf(p));
        absentcount.setText(String.valueOf(a));
        latecount.setText(String.valueOf(l));
        //  student_view.setBackgroundResource(R.color.l);


    }

    public static void statusP(boolean abs, boolean late) {
        p = Integer.parseInt(presentcount.getText().toString()) + 1;

        if (abs) {
            a = Integer.parseInt(absentcount.getText().toString()) - 1;

        } else {

            l = Integer.parseInt(latecount.getText().toString()) - 1;
        }

        presentcount.setText(String.valueOf(p));
        absentcount.setText(String.valueOf(a));
        latecount.setText(String.valueOf(l));
//        student_view.setBackgroundResource(R.color.p);


    }

    public static void statusSingleP(boolean status) {
        p = Integer.parseInt(presentcount.getText().toString()) + 1;

        presentcount.setText(String.valueOf(p));
       /* a = Integer.parseInt(absentcount.getText().toString());
        l = Integer.parseInt(latecount.getText().toString());
       */ /*presentcount.setText(String.valueOf(p));
        absentcount.setText(String.valueOf(a));
        latecount.setText(String.valueOf(l));
*/       // student_view.setBackgroundResource(R.color.p);


    }

    public static void statusSingleA(boolean status) {
        a = Integer.parseInt(absentcount.getText().toString()) + 1;

        absentcount.setText(String.valueOf(a));
       /* p = Integer.parseInt(presentcount.getText().toString());
        l = Integer.parseInt(latecount.getText().toString());
       *//* presentcount.setText(String.valueOf(p));
        absentcount.setText(String.valueOf(a));
        latecount.setText(String.valueOf(l));
*/       // student_view.setBackgroundResource(R.color.a);


    }

    public static void statusSingleL(boolean status) {
        l = Integer.parseInt(latecount.getText().toString()) + 1;

        latecount.setText(String.valueOf(l));
       /* a = Integer.parseInt(absentcount.getText().toString());
        p = Integer.parseInt(presentcount.getText().toString());
        presentcount.setText(String.valueOf(p));
        absentcount.setText(String.valueOf(a));
        latecount.setText(String.valueOf(l));
*/      //  student_view.setBackgroundResource(R.color.l);


    }


}