package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle,inputNoteSubtitle,inputNoteText;
    private TextView textDateTime;
    private TextView textWebURL;
    private LinearLayout layoutWebURL;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    CollectionReference collectionReference;
    public String title = "";
    public String section = "";
    DocumentReference documentReference;
    TextView url_text;
    public String input_note = "";
    private AlertDialog dialogAddURL;
    ImageView image_url;
    public String date_today = "";
    public String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Intent intent  = getIntent();
         title  =intent.getStringExtra("title");
         section = intent.getStringExtra("section");

        ImageView imageBack = findViewById(R.id.ImageBack);
        ImageView imageSave = findViewById(R.id.ImageSave);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);
        image_url = findViewById(R.id.image_url);
        url_text = findViewById(R.id.url_textview);

        date_today = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date());
        System.out.println(date_today);
        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        image_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUrlDialog();
            }
        });
    }

    private void showUrlDialog() {
        AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_add_url, null);
        alerDialog2.setView(view);
        AlertDialog dialog = alerDialog2.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        Button add_button = (Button) view.findViewById(R.id.textAdd);
        Button cancel_button = (Button) view.findViewById(R.id.textCancel);

        EditText input_url = view.findViewById(R.id.inputURL);
        input_url.requestFocus();
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 url = input_url.getText().toString().trim();
                input_note = inputNoteText.getText().toString();

                System.out.println(input_note);



                System.out.println(url);

                if(TextUtils.isEmpty(url))
                {
                    input_url.setError("url is required");

                }
                else if(!Patterns.WEB_URL.matcher(input_url.getText().toString()).matches())
                {

                    input_url.setError("Enter valid url!");


                }
                else
                {

                   url_text.setVisibility(View.VISIBLE);
                   url_text.setText(url);
                    Linkify.addLinks(url_text, Linkify.WEB_URLS);
                    url_text.setLinkTextColor(Color.parseColor("#365D5A"));
                   dialog.dismiss();
                }






            }
        });

    }

    private void saveNote(){
        if(inputNoteTitle.getText().toString().trim().isEmpty()
         && inputNoteText.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Note can't be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            String note_title = inputNoteTitle.getText().toString().trim();
            String note_subtitle = inputNoteSubtitle.getText().toString().trim();
            String my_note = inputNoteText.getText().toString().trim();
            String link = "";
            if(url.isEmpty())
            {
                url = "No url added";
            }


            DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

            documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder fields = new StringBuilder("");
                    status = fields.append(doc.get("choice")).toString();

                    if(status.equals("Professional teacher / Home tutor")){


                        DocumentReference documentReference = firestore.collection("users").document(userID)
                                .collection("All Files").document(PROF)
                                .collection("Courses").document(title)
                                .collection("Sections").document(section).collection("MyNotes").document(note_title);
                        Map<String, Object> user = new HashMap<>();
                        user.put("noteTitle", note_title);
                        user.put("subtitle",note_subtitle);
                        user.put("date",date_today);
                        user.put("mynote",my_note);
                        user.put("url",url);
                        user.put("search",note_title);


                        documentReference.set(user);

                        Intent intent = new Intent(getApplicationContext(),MyNotes.class);
                        intent.putExtra("section",section);
                        intent.putExtra("title",title);
                        startActivity(intent);


                    }
                    else if(!(status.equals("Professional teacher / Home tutor"))){

                        DocumentReference documentReference = firestore.collection("users").document(userID)
                                .collection("Courses").document(title)
                                .collection("Sections").document(section).collection("MyNotes").document(note_title);
                        Map<String, Object> user = new HashMap<>();
                        user.put("noteTitle", note_title);
                        user.put("subtitle",note_subtitle);
                        user.put("date",date_today);
                        user.put("mynote",my_note);
                        user.put("url",url);
                        user.put("search",note_title);


                        documentReference.set(user);

                        Intent intent = new Intent(getApplicationContext(),MyNotes.class);
                        intent.putExtra("section",section);
                        intent.putExtra("title",title);
                        startActivity(intent);


                    }



                }
            }) .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });



        }

    }
}