package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class AddNewClassActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText coursetitle, courseno, semester, credits;
    Button submit;
    RadioGroup radioGroup;
    RadioButton radioButton;
    public String rb = "";
    public String text = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FragmentManager fragmentManager;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    private DocumentReference documentReference;
    //  private ArrayList<CourseInfo> courseInfos;
    String courseTitle = "";
    String courseNo = "";
    String semesterYear = "";
    String creditHour = "";
    String courseType = "";
    String noOfQuizes = "";
    String status = "";
    CourseInfo courseInfo;
    QuerySnapshot querySnapshot;
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_class);
        String fragment = getIntent().getStringExtra("fragment");
        coursetitle = findViewById(R.id.coursetitle);
        courseno = findViewById(R.id.courseno);
        semester = findViewById(R.id.semester);
        credits = findViewById(R.id.credits);
        submit = findViewById(R.id.submit);
        radioGroup = findViewById(R.id.radiogroup);
        //   firebaseDatabase = FirebaseDatabase.getInstance();
        // firebaseUser = firebaseAuth.getCurrentUser();

        Spinner spinner = findViewById(R.id.quizno);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseTitle = coursetitle.getText().toString().trim();
                courseNo = courseno.getText().toString().trim();
                semesterYear = semester.getText().toString().trim();
                creditHour = credits.getText().toString().trim();
                courseType = rb;
                noOfQuizes = text;


                if(status.equals("Professional teacher / Home tutor")){
                    documentReference = firestore.collection("users").document(userID).collection("All Files").document(PROF).collection("Courses").document(courseTitle);
                }
                else{
                    documentReference = firestore.collection("users").document(userID).collection("Courses").document(courseTitle);
                }


                if (TextUtils.isEmpty(courseTitle)) {
                    coursetitle.setError("Course title is required");

                    return;
                }
                if (TextUtils.isEmpty(courseNo)) {
                    courseno.setError("Course number is required");

                    return;
                }
                if (TextUtils.isEmpty(semesterYear)) {
                    semester.setError("Semester is required");

                    return;
                }
                if (TextUtils.isEmpty(creditHour)) {
                    credits.setError("Credit hours are required");

                    return;
                }
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select the course type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (spinner.getSelectedItem() == null) {
                    Toast.makeText(getApplicationContext(), "Please select the number of quizes!", Toast.LENGTH_SHORT).show();
                    return;
                }
              /*          documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                       // ArrayList<CourseInfo> toArrayList =  (ArrayList<CourseInfo>)document.getData();
                                        HashMap<String,Object> hi = (HashMap<String,Object>) document.getData();


                                    }
                                }
                            }
                        });*/

                Map<String, Object> user = new HashMap<>();
                user.put("courseTitle", courseTitle);
                user.put("courseNo", courseNo);
                user.put("semester", semesterYear);
                user.put("credits", creditHour);
                user.put("courseType", courseType);
                user.put("noOfQuizes", noOfQuizes);


                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNewClassActivity.this, "The course is added!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Features.class);
                        intent.putExtra("fragment", "ClassesFragment");

                        startActivity(intent);

                    }
                });

                //  getSupportFragmentManager().beginTransaction().add(R.id.container_main, c).addToBackStack(null).commit(); ei j eta comment kora

// call dhorook
          /*      ArrayList<CourseInfo> courseInfos = new ArrayList<>();
              //  Collections.addAll()
                CourseInfo courseInfo = new CourseInfo(courseTitle,courseNo,semesterYear,creditHour,courseType,noOfQuizes);
                courseInfos.add(courseInfo);
                documentReference.set(courseInfos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNewClassActivity.this, "The course is added!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), Features.class);
                        intent.putExtra("fragment", "ClassesFragment");
                        startActivity(intent);
                    }
                });*/
                //  CourseInfo courseInfo = new CourseInfo(courseTitle,courseNo,semesterYear,creditHour,courseType,noOfQuizes,courseInfos);
                //    databaseReference = FirebaseDatabase.getInstance().getReference();
        /*        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getUid()).child("choice").child("courses");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            InformationToAddNewCourse informationToAddNewCourse = new InformationToAddNewCourse(courseTitle, courseNo, semesterYear, creditHour, courseType, noOfQuizes);
                            databaseReference.setValue(informationToAddNewCourse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(getApplicationContext(), Features.class);
                                    intent.putExtra("fragment", "ClassesFragment");
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

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();
        System.out.println(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void radiobutton(View view) {
        int radioID = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioID);
        rb = radioButton.getText().toString();
        System.out.println(rb);

    }


}
