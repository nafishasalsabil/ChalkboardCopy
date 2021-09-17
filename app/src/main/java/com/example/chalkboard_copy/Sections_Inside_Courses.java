package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class Sections_Inside_Courses extends AppCompatActivity {
    TextView t1;
    private RecyclerView recyclerView;
    private SectionAdapter sectionAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    EditText section_name;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    public static String title  = "";
    List<SectionClass> sectionitems = new ArrayList<>();
    Toolbar section_toolbar;
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    CollectionReference collectionReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections__inside__courses);
        t1 = findViewById(R.id.ash1);
        section_toolbar = findViewById(R.id.toolbar_section);
        setSupportActionBar(section_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        section_toolbar.setNavigationIcon(R.drawable.ic_back);
        recyclerView = (RecyclerView) findViewById(R.id.section_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
         title = intent.getStringExtra("Title");
        getSupportActionBar().setTitle(title);
        section_toolbar.setTitleTextColor(Color.BLACK);
        firestore = FirebaseFirestore.getInstance();
        userID =  firebaseAuth.getCurrentUser().getUid();
        System.out.println(userID);
        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){

                    collectionReference = firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(PROF).collection("Courses").document(title).collection("Sections");


                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    collectionReference = firestore.collection("users")
                            .document(userID).collection("Courses").document(title).collection("Sections");



                }

                System.out.println(collectionReference);
                Query query = collectionReference;
                FirestoreRecyclerOptions<SectionClass> options = new FirestoreRecyclerOptions.Builder<SectionClass>()
                        .setQuery(query, SectionClass.class)
                        .build();
                sectionAdapter = new SectionAdapter(options);
                sectionAdapter.startListening();
                recyclerView.setAdapter(sectionAdapter);
                // classitems.addAll(options);

                    t1.setVisibility(View.GONE);

                sectionAdapter.notifyDataSetChanged();



            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });









        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.add_section_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSectionDialog();
            }
        });
        section_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
          //      intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                finish();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
        sectionAdapter.stopListening();
    }
    List<String> archive = new ArrayList<>();
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
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                sectionAdapter.deleteItem(viewHolder.getAdapterPosition());

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

    private void showSectionDialog() {
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.section_dialog_box, null);
        alerDialog.setView(view);
        AlertDialog dialog = alerDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        Button add_sec =(Button) view.findViewById(R.id.section_add_button);
        section_name =(EditText) view.findViewById(R.id.section_edittext);
        add_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_name = section_name.getText().toString();
                System.out.println(s_name);

                if (TextUtils.isEmpty(s_name))
                {
                    section_name.setError("Section is required");
                    return;
                }
                System.out.println(title);
                DocumentReference documentReference;

                if(status.equals("Professional teacher / Home tutor")){

                    documentReference = firestore.collection("users").document(userID).collection("All Files").document(PROF).collection("Courses").document(title).collection("Sections").document(s_name);

                }
                else{
                    documentReference = firestore.collection("users").document(userID)
                            .collection("Courses").document(title).collection("Sections").document(s_name);

                }



                Map<String, Object> user = new HashMap<>();
                user.put("section", s_name);
                documentReference.set(user);

                dialog.dismiss();

            }
        });

    }
}