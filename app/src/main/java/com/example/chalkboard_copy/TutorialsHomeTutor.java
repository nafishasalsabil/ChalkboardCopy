package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TutorialsHomeTutor extends AppCompatActivity {
    public static final int REQEST_CODE_ADD_LINK = 1;
    RecyclerView tutorials_recyclerview;
    public static final int NUM_COLUMN = 2;
    private LinearLayout layoutWebLINK;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();

    List<TutorialsClass> tutorialsClassList = new ArrayList();
    TutorialHomeTutorAdapter tutorialAdapter;
    FloatingActionButton imageAddLinkMain;
    public String tutorial_title  ="";
    public String tutorial_link  ="";

    public String course_title = "";
    public String course_section = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials_home_tutor);
        imageAddLinkMain = findViewById(R.id.addlinks_ht);
        Toolbar toolbar_tutorials = findViewById(R.id.toolbar_tutorials_ht);
        setSupportActionBar(toolbar_tutorials);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_tutorials.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Tutorials");
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar_tutorials.setTitleTextColor(Color.BLACK);
        //  getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_tutorials.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent  = getIntent();
        course_title = intent.getStringExtra("title");
        course_section = intent.getStringExtra("section");

        tutorials_recyclerview = findViewById(R.id.tutorialsRecyclerView_ht);
        tutorials_recyclerview.setHasFixedSize(true);
        tutorials_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CollectionReference collectionReference = firestore.collection("users").document(userID)
                .collection("Courses").document(course_title).collection("Batches")
                .document(course_section)
                .collection("Tutorials");
        Query query = collectionReference;
        FirestoreRecyclerOptions<TutorialsClass> options = new FirestoreRecyclerOptions.Builder<TutorialsClass>()
                .setQuery(query, TutorialsClass.class)
                .build();
        tutorialAdapter = new TutorialHomeTutorAdapter(options);
        tutorials_recyclerview.setAdapter(tutorialAdapter);
        // classitems.addAll(options);
        tutorialAdapter.notifyDataSetChanged();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(tutorials_recyclerview);
        imageAddLinkMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(TutorialsHomeTutor.this);
                View view = LayoutInflater.from(TutorialsHomeTutor.this).inflate(R.layout.layout_add_tutorial_links, null);
                alerDialog2.setView(view);
                AlertDialog dialog = alerDialog2.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                Button add_button = (Button) view.findViewById(R.id.textAdd);
                Button cancel_button = (Button) view.findViewById(R.id.textCancel);

                EditText tutorial_name_edit_text = view.findViewById(R.id.link_name);
                EditText input_url = view.findViewById(R.id.link_to_add);
             /*   tutorialAdapter = new TutorialAdapter(getApplicationContext());
                users_recycler_view.setAdapter(chatUserAdapter);
                chatUserAdapter.notifyDataSetChanged();
*/
//                input_url.requestFocus();
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tutorial_title = tutorial_name_edit_text.getText().toString().trim();
                        tutorial_link = input_url.getText().toString().trim();


                        // inputNoteText.setText(inputNoteText + "" + url);

                        System.out.println(tutorial_title);
                        if(TextUtils.isEmpty(tutorial_title))
                        {
                            tutorial_name_edit_text.setError("url is required");
                            //  Toast.makeText(getApplicationContext(),"Empty!",Toast.LENGTH_LONG);
                        }

                        if(TextUtils.isEmpty(tutorial_link))
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


                            DocumentReference documentReference = firestore.collection("users").document(userID)
                                    .collection("Courses").document(course_title).collection("Batches")
                                    .document(course_section)
                                    .collection("Tutorials").document(tutorial_title);
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    TutorialsClass tutorialsClass = new TutorialsClass(tutorial_title,tutorial_link);
                                    documentReference.set(tutorialsClass);
                                 /*   tutorials_recyclerview.setAdapter(tutorialAdapter);
                                    tutorialsClassList.add(tutorialsClass);
                                    tutorialAdapter.notifyDataSetChanged();
                                  */  Toast.makeText(getApplicationContext(), "The course is added!", Toast.LENGTH_SHORT).show();

                                }
                            });

                            dialog.dismiss();
                        }






                    }
                });

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        tutorialAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        tutorialAdapter.stopListening();
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //   classAdapter.deleteItem(viewHolder.getAdapterPosition());
            if(direction== ItemTouchHelper.LEFT)
            {
                Toast.makeText(getApplicationContext(), "Deleting", Toast.LENGTH_LONG).show();
                tutorialAdapter.deleteItem(viewHolder.getAdapterPosition());

            }



        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.absent))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


}