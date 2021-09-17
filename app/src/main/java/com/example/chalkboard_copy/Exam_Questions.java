package com.example.chalkboard_copy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class Exam_Questions extends AppCompatActivity {
    ListView myexampdfListView;
    DocumentReference documentReference;
    //   List<PDFs> PDFs;
    List<UploadPDFClass> uploadPDFClassList;
    Toolbar exam_toolbar;
    FloatingActionButton floatingActionButton;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";

    public String section = "";
    public String title = "";
    public String pdf = "";
    public ArrayAdapter<String> adapter;
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam__questions);
        exam_toolbar = findViewById(R.id.toolbar_exam);
        myexampdfListView = (ListView) findViewById(R.id.myListView_exam);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        section = intent.getStringExtra("section");
        uploadPDFClassList = new ArrayList<>();
        floatingActionButton = findViewById(R.id.addquestion);
        setSupportActionBar(exam_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Exam Questions");
        exam_toolbar.setTitleTextColor(Color.BLACK);
        exam_toolbar.setNavigationIcon(R.drawable.ic_back);
        exam_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(Exam_Questions.this);
                View view = LayoutInflater.from(Exam_Questions.this).inflate(R.layout.custom_dialogbox_for_adding_pdf_title, null);
                alerDialog2.setView(view);
                AlertDialog dialog = alerDialog2.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                Button done_button = (Button) view.findViewById(R.id.done_pdf);
                Button cancel = (Button) view.findViewById(R.id.cancel_pdf);
                EditText pdf_title = view.findViewById(R.id.pdf_name_edittext);

                done_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pdf = pdf_title.getText().toString();
                        if (TextUtils.isEmpty(pdf)) {
                            pdf_title.setError("You must add a name!");
                        }
                        selectPDF();
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                    }
                });


            }
        });
        viewAllFiles();
        myexampdfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UploadPDFClass uploadPDFClass = uploadPDFClassList.get(position);
//               Intent intent1 = new Intent();
//               intent1.setType(Intent.ACTION_VIEW);
//               intent1.setData(Uri.parse(uploadPDFClass.getPdf_url()));
//               startActivity(intent1);

//                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(uploadPDFClass.getPdf_url()), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void viewAllFiles() {

        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){

                    collectionReference = firestore.collection("users").document(userID)
                            .collection("All Files").document(PROF).collection("Courses")
                            .document(title).collection("Sections").document(section)
                            .collection("Exam_questions");


                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    collectionReference = firestore.collection("users").document(userID)
                            .collection("Courses").document(title).collection("Sections")
                            .document(section)
                            .collection("Exam_questions");


                }



                collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<UploadPDFClass> documentData = queryDocumentSnapshots.toObjects(UploadPDFClass.class);
                        uploadPDFClassList.clear();
                        uploadPDFClassList.addAll(documentData);
                        String[] uploads = new String[uploadPDFClassList.size()];
                        for (int i = 0; i < uploads.length; i++) {
                            uploads[i] = uploadPDFClassList.get(i).getPdf_name();
                        }

                        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.exam_question_item, R.id.pdf_name_exam, uploads) {
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                View view = super.getView(position, convertView, parent);
                                //     TextView textView = view.findViewById(R.id.pdf_name);
                                return view;
                            }
                        };
                        myexampdfListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                });




            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });






    }
    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select PDF file!"),1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            uploadPDFfile(data.getData());
        }
    }

    private void uploadPDFfile(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading pdf file...");
        progressDialog.show();
        StorageReference storageReference1 = storageReference.child("pdfs/" +System.currentTimeMillis()+".pdf");


        storageReference1.putFile(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                while (!uri.isComplete());
                Uri url = uri.getResult();
                UploadPDFClass uploadPDFClass = new UploadPDFClass(pdf,url.toString());


                Log.d("checked","at data changed"+uploadPDFClassList.size());



                DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

                documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        StringBuilder fields = new StringBuilder("");
                        status = fields.append(doc.get("choice")).toString();

                        if(status.equals("Professional teacher / Home tutor")){

                            documentReference = firestore.collection("users").document(userID)
                                    .collection("All Files")
                                    .document(PROF).collection("Courses")
                                    .document(title).collection("Sections").document(section)
                                    .collection("Exam_questions").document(pdf);

                            documentReference.set(uploadPDFClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "The pdf is added!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    viewAllFiles();


                                }
                            });


                        }
                        else if(!(status.equals("Professional teacher / Home tutor"))){

                            documentReference = firestore.collection("users").document(userID)
                                    .collection("Courses").document(title)
                                    .collection("Sections").document(section)
                                    .collection("Exam_questions").document(pdf);
                            documentReference.set(uploadPDFClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "The pdf is added!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    viewAllFiles();


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
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                progressDialog.setMessage((int)progress + "%");

            }
        });
    }
}