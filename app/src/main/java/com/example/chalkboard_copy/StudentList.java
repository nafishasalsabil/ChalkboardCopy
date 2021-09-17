package com.example.chalkboard_copy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/*import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.sql.Types.BOOLEAN;
import static java.sql.Types.NUMERIC;
import static javax.xml.xpath.XPathConstants.STRING;

public class StudentList extends AppCompatActivity {

    com.getbase.floatingactionbutton.FloatingActionsMenu fab;
    com.getbase.floatingactionbutton.FloatingActionsMenu add_new_student_fab_menu;
    com.getbase.floatingactionbutton.FloatingActionButton add_student_new_manually;
    com.getbase.floatingactionbutton.FloatingActionButton import_excel_sheet;
    com.getbase.floatingactionbutton.FloatingActionButton add_student;
    List<StudentItems> studentItems = new ArrayList<>();
    private DocumentReference documentReference, documentReference2, studentsdocument, documentReference3;
    private CollectionReference collectionReference, studentcollection;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
 String clicked_courseTitle = "";
 String clicked_course_section = "";
    EditText id, name;
    DatePickerDialog datePickerDialog;
    TextView t1, t2;
    private static final int REQUEST_CODE = 5795;
    private static final String TAG = "checked";

    private RecyclerView recyclerView;
    private StudentListAdapter studentListAdapter;
    private ClassAdapter classAdapter;
    private RecyclerView.LayoutManager layoutManager;
Button done;
    public static String Lecture_s = "";
    public final String BOTHACCOUNT="Professional teacher / Home tutor";

    StudentItems studentItems_object = new StudentItems();
    CourseInfo courseInfo = new CourseInfo();
    public static int id1;
    Toolbar toolbar_list;
    public static String status = "";
    public static String status_attendance = "";

    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    DocumentReference retrievesection;
    public static String filename="";
    public static String path_str="";
    com.getbase.floatingactionbutton.FloatingActionButton take_attendance;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Intent intent = getIntent();
        clicked_courseTitle = intent.getStringExtra("title");
        clicked_course_section = intent.getStringExtra("section");
        System.out.println(clicked_courseTitle);
        System.out.println(clicked_course_section);
        recyclerView = (RecyclerView) findViewById(R.id.students_list_recycler_view);
        t1 = findViewById(R.id.tv01);
        t2 = findViewById(R.id.tv02);
        take_attendance = findViewById(R.id.take_attendance_fab);
        toolbar_list = findViewById(R.id.toolbar_list);
        setSupportActionBar(toolbar_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_list.setNavigationIcon(R.drawable.ic_back);
        toolbar_list.setTitle(clicked_course_section);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Students");
        toolbar_list.setTitleTextColor(Color.BLACK);
        toolbar_list.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(),InsideClassActivity.class);
                intent2.putExtra("Title", clicked_courseTitle);
                intent2.putExtra("section", clicked_course_section);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
            }
        });
        add_new_student_fab_menu = findViewById(R.id.add_new_student_fab_menu);
        add_student_new_manually = findViewById(R.id.add_student_manually_fab);
        import_excel_sheet = findViewById(R.id.import_from_excel);
        fab = findViewById(R.id.main_fab);
        done = findViewById(R.id.done_button);
        add_student = findViewById(R.id.add_student_fab);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        done.setVisibility(View.GONE);



        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();
                System.out.println(status+"___________Loading?");
                System.out.println(status+" Check");
                if(status.equals("Professional teacher / Home tutor")){

                    retrievesection = firestore.collection("users").document(userID).collection("All Files").document(PROF).collection("Courses").document(clicked_courseTitle);
                    System.out.println("Working??????????????????????????");
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

                    studentcollection = firestore.collection("users").document(userID).collection("All Files").document(PROF)
                            .collection("Courses").document(clicked_courseTitle).collection("Sections")
                            .document(clicked_course_section)
                            .collection("Students");
                    System.out.println("Check Student collection right`````````````````````");

                    studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                            studentListAdapter = new StudentListAdapter(getApplicationContext(), studentItems);
                            recyclerView.setAdapter(studentListAdapter);
                            studentItems.addAll(documentData);
                            studentListAdapter.notifyDataSetChanged();
                            if(studentItems.size()==0){
                                t1.setVisibility(View.VISIBLE);
                                t2.setVisibility(View.VISIBLE);
                                done.setVisibility(View.GONE);
                            }
                            else{
                                t1.setVisibility(View.GONE);
                                t2.setVisibility(View.GONE);
                                done.setVisibility(View.GONE);
                            }

                            add_new_student_fab_menu.setVisibility(View.GONE);
                            for (int i = 0; i < studentItems.size(); i++) {
                                System.out.println(studentItems.get(i).toString());
                            }

                        }
                    });



                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    retrievesection = firestore.collection("users")
                            .document(userID).collection("Courses").document(clicked_courseTitle);
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
                            .collection("Courses").document(clicked_courseTitle).collection("Sections")
                            .document(clicked_course_section)
                            .collection("Students");
                    System.out.println("Check Student collection wrong`````````````````````");
                    studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                            studentListAdapter = new StudentListAdapter(getApplicationContext(), studentItems);
                            recyclerView.setAdapter(studentListAdapter);
                            studentItems.addAll(documentData);
                            studentListAdapter.notifyDataSetChanged();
                            if(studentItems.size()==0){
                                t1.setVisibility(View.VISIBLE);
                                t2.setVisibility(View.VISIBLE);
                                done.setVisibility(View.GONE);
                            }
                            else{
                                t1.setVisibility(View.GONE);
                                t2.setVisibility(View.GONE);
                                done.setVisibility(View.GONE);
                            }

                            add_new_student_fab_menu.setVisibility(View.GONE);
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






    /*    if(status.equals("Professional teacher / Home tutor")){
            studentcollection = firestore.collection("users").document(userID).collection("All Files").document(PROF)
                    .collection("Courses").document(clicked_courseTitle).collection("Sections")
                    .document(clicked_course_section)
                    .collection("Students");
            System.out.println("Check Student collection right`````````````````````");

            studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                    studentListAdapter = new StudentListAdapter(getApplicationContext(), studentItems);
                    recyclerView.setAdapter(studentListAdapter);
                    studentItems.addAll(documentData);
                    studentListAdapter.notifyDataSetChanged();
                    if(studentItems.size()==0){
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    }
                    else{
                        t1.setVisibility(View.GONE);
                        t2.setVisibility(View.GONE);
                        done.setVisibility(View.GONE);
                    }

                    add_new_student_fab_menu.setVisibility(View.GONE);
                    for (int i = 0; i < studentItems.size(); i++) {
                        System.out.println(studentItems.get(i).toString());
                    }

                }
            });


        }
        else{
            studentcollection = firestore.collection("users").document(userID)
                    .collection("Courses").document(clicked_courseTitle).collection("Sections")
                    .document(clicked_course_section)
                    .collection("Students");
            System.out.println("Check Student collection wrong`````````````````````");
            studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                    studentListAdapter = new StudentListAdapter(getApplicationContext(), studentItems);
                    recyclerView.setAdapter(studentListAdapter);
                    studentItems.addAll(documentData);
                    studentListAdapter.notifyDataSetChanged();
                    if(studentItems.size()==0){
                        t1.setVisibility(View.VISIBLE);
                        t2.setVisibility(View.VISIBLE);
                        done.setVisibility(View.GONE);
                    }
                    else{
                        t1.setVisibility(View.GONE);
                        t2.setVisibility(View.GONE);
                        done.setVisibility(View.GONE);
                    }

                    add_new_student_fab_menu.setVisibility(View.GONE);
                    for (int i = 0; i < studentItems.size(); i++) {
                        System.out.println(studentItems.get(i).toString());
                    }

                }
            });


        }
*/

        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_func();
            }
        });
        add_student_new_manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        import_excel_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooser();
             //   readExcelFile(getApplicationContext(), "myExcel.xlsx");




              /*  try {
// OPTION 1: if the file is in the sd
                    File csvfile = new File(Environment.getExternalStorageDirectory()+"" + "/myExcel.xlsx");
// END OF OPTION 1

// OPTION 2: pack the file with the app
                 //    "If you want to package the .csv file with the application and have it install on the internal storage when the app installs, create an assets folder in your project src/main folder (e.g., c:\myapp\app\src\main\assets\), and put the .csv file in there, then reference it like this in your activity:" (from the cited answer)
                    String csvfileString = getApplicationContext().getApplicationInfo().dataDir + File.separatorChar + "csvfile.csv";
                    File csvfile2 = new File(csvfileString);
// END OF OPTION 2

                    CSVReader reader = new CSVReader(new FileReader(csvfile.getAbsolutePath()));
                    String[] nextLine;
                    while ((nextLine = reader.readNext()) != null) {
                        // nextLine[] is an array of values from the line
                        System.out.println(nextLine[0] + nextLine[1] + "etc...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "The specified file was not found", Toast.LENGTH_SHORT).show();
                }*/

                // inputText.setText(myData);
                // response.setText("SampleFile.txt data retrieved from Internal Storage...");

            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDoneDialog();

            }
        });


        take_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentListAdapter.getItemCount() == 0) {

                    showNoStudentsToTakeAttendanceDialog();
                } else {

                    showNoticeDialog();

                }

            }
        });



        }










    ArrayList<String> FOLDERS = new ArrayList<String>();
    ArrayList<String> XLS_FILES = new ArrayList<String>();

    int CALL_COUNT = 0;

    public void getXLSFiles(String path) {

        try {

            File dirFolder = new File(path);
            File[] folders = dirFolder.listFiles();

            for (File file : folders) {
                if (file.isDirectory()) {
                    FOLDERS.add(path + file.getName() + "/");
                } else {

                    if (file.getName().contains(".xlsx")) {

                        XLS_FILES.add(path + file.getName());
                        filename = file.getName();
                        System.out.println("hi "+filename);
                        path_str =path ;
                        readExcelFile(getApplicationContext(),filename);



                    } else {
                        // FILE_PATH = path + file.getName();
                        // Log.e("File Deatils", "" + FILE_PATH);
                    }
                }

                for(int i=0;i<XLS_FILES.size();i++){
                    System.out.println(XLS_FILES.get(i));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.see_records) {
            Intent intent1 = new Intent(getApplicationContext(), Attendance_record.class);
            intent1.putExtra("title", clicked_courseTitle);
            intent1.putExtra("section", clicked_course_section);
            startActivity(intent1);
        }
        return true;
    }
    private void showNoticeDialog() {
        AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.notice_dialogbox, null);
        alerDialog2.setView(view);
        final AlertDialog dialog = alerDialog2.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        Button yes_button = (Button) view.findViewById(R.id.yes_button);
        Button no = (Button) view.findViewById(R.id.no_button);

        yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_func();
                dialog.dismiss();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateAndLectureDialog();
                dialog.dismiss();


            }
        });


    }
    private void showDoneDialog() {
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialogbox, null);
        alerDialog.setView(view);
        final AlertDialog dialog = alerDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();


        Button done_done = view.findViewById(R.id.done_done);
        Button add_more = view.findViewById(R.id.add_more);

        done_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*add_new_student_fab_menu.setVisibility(View.INVISIBLE);
                done.setVisibility(View.INVISIBLE);
                fab.setVisibility(View.VISIBLE);
                add_student.setEnabled(false);
                add_student.setVisibility(View.GONE);

*/
                done.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
                add_student.setVisibility(View.GONE);
                add_new_student_fab_menu.setVisibility(View.GONE);
                dialog.dismiss();

            }
        });
        add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

    }
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_new_students, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        id = view.findViewById(R.id.student_id);
        name = view.findViewById(R.id.student_name);

        Button cancel = view.findViewById(R.id.cancelbutton);
        Button add = view.findViewById(R.id.add);

        cancel.setOnClickListener(v -> dialog.dismiss());
        add.setOnClickListener((View v) -> {
           // addstudent();
         /*   present.setVisibility(View.VISIBLE);
            absent.setVisibility(View.VISIBLE);
            late.setVisibility(View.VISIBLE);
         */
          /*  t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);
          */  fab.setVisibility(View.INVISIBLE);
            String d_d = studentItems_object.getLecture_date();
            if(TextUtils.isEmpty(id.getText()))
            {
                id.setError("Empty!");
            }
            else if(TextUtils.isEmpty(name.getText()))
            {
                name.setError("Empty!");
            }
            else {

                id1 = Integer.parseInt(id.getText().toString());
                String name1 = name.getText().toString();

                String l_l = studentItems_object.getLecture_name();
                studentItems_object.setName(name1);
                studentItems_object.setId(id1);
                studentItems.add(new StudentItems(id1, name1, "not_taken", l_l, d_d));
                studentListAdapter.notifyDataSetChanged();

                if(status.equals(BOTHACCOUNT)){
                    studentsdocument = firestore.collection("users").document(userID)
                            .collection("All Files").document(PROF)
                            .collection("Courses").document(clicked_courseTitle)
                            .collection("Sections").document(clicked_course_section)
                            .collection("Students").document(Integer.toString(id1));
                }
                else{
                    studentsdocument = firestore.collection("users").document(userID)
                            .collection("Courses").document(clicked_courseTitle)
                            .collection("Sections").document(clicked_course_section)
                            .collection("Students").document(Integer.toString(id1));
                }


                Map<String, Object> inuser = new HashMap<>();
                inuser.put("id", id1);
                inuser.put("name", name1);

                studentsdocument.set(inuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "The student is added!", Toast.LENGTH_SHORT).show();

                        if(status.equals(BOTHACCOUNT)){
                            DocumentReference documentReference4 = firestore.collection("users").document(userID) .collection("All Files").document(PROF)
                                    .collection("Courses").document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                                    .collection("Class_Performance").document(Integer.toString(id1));
                            Map<String, Object> perf = new HashMap<>();
                            perf.put("id", id1);
                            perf.put("name", name1);
                            perf.put("count",0);
                            documentReference4.set(perf);
                            id.getText().clear();
                            name.getText().clear();
                          //  dialog.dismiss();
                        }
                        else{
                            DocumentReference documentReference4 = firestore.collection("users").document(userID)
                                    .collection("Courses").document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                                    .collection("Class_Performance").document(Integer.toString(id1));
                            Map<String, Object> perf = new HashMap<>();
                            perf.put("id", id1);
                            perf.put("name", name1);
                            perf.put("count",0);
                            documentReference4.set(perf);
                            id.getText().clear();
                            name.getText().clear();
                           // dialog.dismiss();
                        }


                    }
                });
            }
            //  StudentItems studentItems = new StudentItems();

//        System.out.println(Lecture);
            //String lec = lecture_method();
            //  lecture_method();




                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);





        });
    }
    private void addstudent() {
        //object.setStudent_name(name1);
        //  object.setStudent_id(id1);


    }
    private void showDateAndLectureDialog() {
        AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.lecture_and_date, null);
        alerDialog2.setView(view);
        AlertDialog dialog = alerDialog2.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        TextView lecture_text = view.findViewById(R.id.lecture_edittext);
        Button done_button = (Button) view.findViewById(R.id.done_lecture);
        TextView date_textview = view.findViewById(R.id.date_textview);
        final Calendar newCalender = Calendar.getInstance();

        date_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(StudentList.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        String date_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        date_textview.setText(date_date);
                        studentItems_object.setLecture_date(date_date);


                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

              /*  DatePicker datePicker = new DatePicker(StudentList.this);
                int day = datePicker.getDayOfMonth();
                int month = (datePicker.getMonth()) + 1;
                int year = datePicker.getYear();

                datePickerDialog = new DatePickerDialog(StudentList.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        date_textview.setText(date_date);
//                        object.setLecture_date(date_date);
                        studentItems_object.setLecture_date(date_date);

                    }
                }, day, month, year);
                datePickerDialog.show();*/
            }
        });


        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   detect2 = "make_visible";
                studentAdapter.setUi(detect2);
                p = 0;
                l = 0;
                a = 0;
                presentcount.setText(String.valueOf(p));
                absentcount.setText(String.valueOf(a));
                latecount.setText(String.valueOf(l));
                studentAdapter.notifyDataSetChanged();
                present.setVisibility(View.VISIBLE);
                absent.setVisibility(View.VISIBLE);
                late.setVisibility(View.VISIBLE);
                fab.setVisibility(View.GONE);
                toolbar_attendance.setVisibility(View.GONE);
                attendance_done_fab.setVisibility(View.VISIBLE);
             */   //   fab.setVisibility(View.INVISIBLE);
                Lecture_s = lecture_text.getText().toString().trim();
                //  studentAdapter.setLectureName(Lecture_s);
             //   studentAdapter.notifyDataSetChanged();
                //    System.out.println(Lecture);
                // lecture_method(Lecture);
                //         object.setLecture_name(Lecture_s);
                studentItems_object.setLecture_name(Lecture_s);
                //      System.out.println(object.getStudent_name());

                String s_name = studentItems_object.getName();
                int s_id = studentItems_object.getId();
                System.out.println(studentItems_object.getId());
                String d_d = studentItems_object.getLecture_date();
                if(status.equals(BOTHACCOUNT)){
                    documentReference2 = firestore.collection("users").document(userID).collection("All Files").document(PROF).collection("Courses")
                            .document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                            .collection("Attendance")
                            .document(Lecture_s);
                }
                else{
                    documentReference2 = firestore.collection("users").document(userID).collection("Courses")
                            .document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                            .collection("Attendance")
                            .document(Lecture_s);

                }
                Map<String, Object> inuser = new HashMap<>();
                inuser.put("lecture_name", Lecture_s);
                inuser.put("lecture_date", d_d);


                documentReference2.set(inuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //   Toast.makeText(Attendance_activity.this, "The student is added!", Toast.LENGTH_SHORT).show();
                        if(status.equals(BOTHACCOUNT)){
                            studentcollection = firestore.collection("users").document(userID).collection("All Files").document(PROF)
                                    .collection("Courses").document(clicked_courseTitle).collection("Sections")
                                    .document(clicked_course_section)
                                    .collection("Students");
                        }
                        else{
                            studentcollection = firestore.collection("users").document(userID)
                                    .collection("Courses").document(clicked_courseTitle).collection("Sections")
                                    .document(clicked_course_section)
                                    .collection("Students");
                        }

                        studentcollection.orderBy("id", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                                studentItems.addAll(documentData);
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                    StudentItems studentItems1 = documentSnapshot.toObject(StudentItems.class);

                                    if(status.equals(BOTHACCOUNT)){
                                        DocumentReference documentReference3 = firestore.collection("users").document(userID).collection("All Files").document(PROF)
                                                .collection("Courses").document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                                                .collection("Attendance").document(Lecture_s).collection("Status").document(Integer.toString(studentItems1.getId()));
                                        Map<String, Object> inuser = new HashMap<>();
                                        inuser.put("id", studentItems1.getId());
                                        inuser.put("name", studentItems1.getName());
                                        inuser.put("status", "not_taken");

                                        documentReference3.set(inuser);
                                    }
                                    else{
                                        DocumentReference documentReference3 = firestore.collection("users").document(userID)
                                                .collection("Courses").document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                                                .collection("Attendance").document(Lecture_s).collection("Status").document(Integer.toString(studentItems1.getId()));
                                        Map<String, Object> inuser = new HashMap<>();
                                        inuser.put("id", studentItems1.getId());
                                        inuser.put("name", studentItems1.getName());
                                        inuser.put("status", "not_taken");

                                        documentReference3.set(inuser);
                                    }





                                }
                            }
                        });



                    }
                });

             /*   documentReference3 = firestore.collection("users").document(userID).collection("Courses")
                        .document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                        .collection("Students")
                        .document(Integer.toString(studentItems_object.getId()));
                //     ArrayList<String> lecturearraylist = getListOfSubjects();
                Map<String, Object> inuser2 = new HashMap<>();
                inuser2.put("lecture_name", Arrays.asList(Lecture_s));
                inuser2.put("lecture_date", d_d);


                documentReference3.set(inuser2, SetOptions.merge());*/

                Intent intent = new Intent(getApplicationContext(),Attendance_activity.class);
                intent.putExtra("title",clicked_courseTitle);
                intent.putExtra("section",clicked_course_section);
                startActivity(intent);
                dialog.dismiss();


            }
        });


    }


    private void showNoStudentsToTakeAttendanceDialog() {
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog_for_taking_attendance_warning, null);
        alerDialog.setView(view);
        AlertDialog dialog = alerDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();


        Button ok = view.findViewById(R.id.ok_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


    }



    private static void readExcelFile(Context context, String filename) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            System.out.println("Check Check");
            return;
        }
     /*   Workbook wb = WorkbookFactory.create(new File("/storage/emulated/0/Android/data/com.example.chalkboardnew/files/myExcel.xlsx"));
        Sheet mySheet = wb.getSheetAt(0);
        Iterator<Row> rowIter = mySheet.rowIterator();
        System.out.println(mySheet.getRow(1).getCell(0));
     */



      /*  try {

            System.out.println(path_str+""+filename);
           *//* FileInputStream file = new FileInputStream(new File(path_str+""+filename));


            //Get the workbook instance for XLS file
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            System.out.println("Workbook");
            //Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);
            System.out.println("Sheet");

            //Iterate through each rows from first sheet
            Iterator < Row > rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                //For each row, iterate through each columns
                Iterator <Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()) {
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t\t");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t\t");
                            break;
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t\t");
                            break;
                    }
                }
                System.out.println("");
            }
            file.close();
*//*

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

*/














  /*      try {
            // Creating Input Stream
            System.out.println("hi Check");

            FileInputStream file = new FileInputStream(new File("C:\\test.xlsx"));

//Get the workbook instance for XLS file
            XSSFWorkbook workbook = new XSSFWorkbook (file);

//Get first sheet from the workbook
            XSSFSheet sheet = workbook.getSheetAt(0);

//Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = sheet.iterator();

//Get iterator to all cells of current row
            Iterator<Cell> cellIterator = row.cellIterator();
         *//*   File file = new File(path_str,filename);
            FileInputStream myInput = new FileInputStream(file);

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            OPCPackage pkg = OPCPackage.open(file);
//           Package fs = Package.open(new ByteArrayInputStream(filename.getBytes()));
            XSSFWorkbook wb = new XSSFWorkbook(pkg);
//           XSSFSheet sheet = wb.getSheet();
          *//**//*    XSSFRow row;
            XSSFCell cell;*//**//*
            //  * We now need something to iterate through the cells.*
            Iterator<Row> rowIter = mySheet.rowIterator();

            while (rowIter.hasNext()) {
                HSSFRow row = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = row.cellIterator();
                System.out.println("hi Check+++++++");
                while (cellIter.hasNext()) {
                    HSSFCell cell = (HSSFCell) cellIter.next();
                    Log.w("FileUtils", "Cell Value: " + cell.toString());
                    Toast.makeText(context, "cell Value: " + cell.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("hi Check------");
                    System.out.println(cell.toString());
                }
            }*//*
        } catch (Exception e) {
            e.printStackTrace();
        }
*/
        return;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private void add_func() {
  /*      detect1 = "make_invisible";
        studentAdapter.setUi(detect1);
  */      studentListAdapter.notifyDataSetChanged();
      /*  present.setVisibility(View.GONE);
        absent.setVisibility(View.GONE);
        late.setVisibility(View.GONE);
     */   done.setVisibility(View.VISIBLE);
        add_new_student_fab_menu.setVisibility(View.VISIBLE);
        fab.setVisibility(View.GONE);

    }

    private void showChooser() {

        String[] mimetypes =
                { "application/vnd.ms-excel", // .xls
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
                };
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.setType("*/*");
        i.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(i,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            if(data!=null)
            {
                String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + "/";
                System.out.println(SDCARD_PATH);

                Uri uri = data.getData();
                getXLSFiles(SDCARD_PATH);
                Toast.makeText(this,uri.toString(),Toast.LENGTH_LONG);
                Log.d("checked",uri.toString());


            }

        }
    }

}