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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditNoteHT extends AppCompatActivity {
    private EditText inputNoteSubtitle,inputNoteText;
    private TextView inputNoteTitle,textDateTime;
    private TextView textWebURL;
    private LinearLayout layoutWebURL;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();

    public String title = "";
    public String section = "";
    DocumentReference documentReference;
    TextView url_text;
    public String input_note = "";
    private AlertDialog dialogAddURL;
    ImageView image_url;
    public String date_today = "";
    public String url = "";
    String note_name = "";
    public String url_retrieve="";
    public String subtitle_retrieve="";
    public String note_retrieve="";

    DocumentReference documentReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note_h_t);
        Intent intent  = getIntent();
        title  =intent.getStringExtra("title");
        section = intent.getStringExtra("section");
        note_name = intent.getStringExtra("note_name");

        ImageView imageBack = findViewById(R.id.ImageBack_update_ht);
        ImageView imageSave = findViewById(R.id.ImageUpdate_ht);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        inputNoteTitle = findViewById(R.id.inputNoteTitle_update_ht);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle_update_ht);
        inputNoteText = findViewById(R.id.inputNote_update_ht);
        textDateTime = findViewById(R.id.textDateTime_update_ht);
        image_url = findViewById(R.id.image_url_hometutor);
        url_text = findViewById(R.id.url_textview_hometutor);
        DocumentReference documentReference =  firestore.collection("users").document(userID)
                .collection("Courses").document(title)
                .collection("Batches").document(section).collection("MyNotes").document(note_name);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                NotesClass notesClass = documentSnapshot.toObject(NotesClass.class);
                textDateTime.setText(notesClass.getDate());
                inputNoteTitle.setText(notesClass.getNoteTitle());
                inputNoteSubtitle.setText(notesClass.getSubtitle());
                inputNoteText.setText(notesClass.getMynote());
                url_text.setText(notesClass.getUrl());
                if(!(notesClass.getUrl().equals("No url added")))
                {
                    url_text.setVisibility(View.VISIBLE);
                }
                url_retrieve =notesClass.getUrl();
                subtitle_retrieve =notesClass.getSubtitle();
                note_retrieve = notesClass.getMynote();

            }
        });

        documentReference1 = firestore.collection("users").document(userID)
                .collection("Courses").document(title)
                .collection("Batches").document(section).collection("MyNotes").document(note_name);
        date_today = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date());
        System.out.println(date_today);
     /*   textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );*/
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNote();
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
        input_url.setText(url_retrieve);
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

                // inputNoteText.setText(inputNoteText + "" + url);

                System.out.println(url);

                if(TextUtils.isEmpty(url))
                {
                    input_url.setError("url is required");
                    //  Toast.makeText(getApplicationContext(),"Empty!",Toast.LENGTH_LONG);
                }
                else if(!Patterns.WEB_URL.matcher(input_url.getText().toString()).matches())
                {
                    //  Toast.makeText(getApplicationContext(),"Enter valid url!",Toast.LENGTH_LONG);
                    input_url.setError("Enter valid url!");


                }
                else
                {
                   /* String u = inputNoteText.getText().toString() + " " + input_url.getText().toString();
                    inputNoteText.setText(u);
                   */
                    url_text.setVisibility(View.VISIBLE);
                    url_text.setText(url);
                    Linkify.addLinks(url_text, Linkify.WEB_URLS);
                    url_text.setLinkTextColor(Color.parseColor("#365D5A"));
                    dialog.dismiss();
                }






            }
        });

    }

    private void updateNote(){
        if(inputNoteTitle.getText().toString().trim().isEmpty()
                && inputNoteText.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Note can't be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        if (isSubtitleChanged() && isNoteChanged() && isUrlChanged()|| isSubtitleChanged() && isNoteChanged() || isNoteChanged() && isUrlChanged()
                || isUrlChanged() && isSubtitleChanged() || isSubtitleChanged()
                || isNoteChanged() || isUrlChanged()) {
            documentReference1.update("date",date_today);
            Toast.makeText(this, "Your profile has been updated!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),MyNotes.class);
            intent.putExtra("section",section);
            intent.putExtra("title",title);
            startActivity(intent);

        }

    }
    private boolean isSubtitleChanged() {
        if (!subtitle_retrieve.equals(inputNoteSubtitle.getText().toString())) {
            //  usernameref.child("password").setValue(password.getText().toString());
            String changesubtitle = inputNoteSubtitle.getText().toString();
            documentReference1.update("subtitle",changesubtitle);
            return true;
        } else {
            return false;
        }
    }

    private boolean isNoteChanged() {
        if (!note_retrieve.equals(inputNoteText.getText().toString())) {
            //  usernameref.child("password").setValue(password.getText().toString());
            String changenote = inputNoteText.getText().toString();
            documentReference1.update("mynote",changenote);
            return true;
        } else {
            return false;
        }
    }

    private boolean isUrlChanged() {
        if (!url_retrieve.equals(url_text.getText().toString())) {
            //  usernameref.child("password").setValue(password.getText().toString());
            String changeurl = url_text.getText().toString();
            documentReference1.update("url",changeurl);
            return true;
        } else {
            return false;
        }
    }

}