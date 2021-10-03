package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MyNotes_hometutor extends AppCompatActivity {
    public static final int REQEST_CODE_ADD_NOTE = 1;
    RecyclerView notes_recyclerview;
    public static final int NUM_COLUMN = 2;
    private LinearLayout layoutWebURL;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    String title = "";
    String sec = "";
    List<NotesClass> notesClassList = new ArrayList();
Toolbar toolbar1;
    NotesHomeTutorAdapter notesAdapter;
    EditText search_notes;
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    CollectionReference collectionReference;
    public static String note_title="";
    public static String getNote_title() {
        return note_title;
    }

    public static void setNote_title(String note_title) {
        MyNotes.note_title = note_title;
        System.out.println(note_title);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes_hometutor);
        toolbar1 = findViewById(R.id.toolbar_notes);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Notes");
        toolbar1.setTitleTextColor(Color.BLACK);
        toolbar1.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Materials_hometutor.class);
                intent1.putExtra("title",title);
                intent1.putExtra("section",sec);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);

                finish();
            }
        });
        Intent intent  = getIntent();
        title  =intent.getStringExtra("title");
        sec = intent.getStringExtra("section");
        search_notes =findViewById(R.id.inputSearch_hometutor);
        search_notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

              //  searchNotes(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {
                DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

                documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        StringBuilder fields = new StringBuilder("");
                        status = fields.append(doc.get("choice")).toString();

                        if(status.equals("Professional teacher / Home tutor")){

                            collectionReference = firestore.collection("users").document(userID)
                                    .collection("All Files").document(HT)
                                    .collection("Courses").document(title)
                                    .collection("Batches").document(sec).collection("MyNotes");

                            if(s.toString().isEmpty()){

                                Query query = collectionReference;
                                FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                                        .setQuery(query, NotesClass.class)
                                        .build();
                                notesAdapter.updateOptions(options);
                                notesAdapter.startListening();

                            }

                            else{


                                Query query = collectionReference.whereEqualTo("noteTitle",s.toString());
                                FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                                        .setQuery(query, NotesClass.class)
                                        .build();
                                notesAdapter.updateOptions(options);
                                notesAdapter.startListening();


                            }
                        }
                        else if(!(status.equals("Professional teacher / Home tutor"))){
                            collectionReference =  firestore.collection("users").document(userID)
                                    .collection("Courses").document(title)
                                    .collection("Batches").document(sec).collection("MyNotes");

                            if(s.toString().isEmpty()){
                                Query query = collectionReference;
                                FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                                        .setQuery(query, NotesClass.class)
                                        .build();
                                notesAdapter.updateOptions(options);
                                notesAdapter.startListening();
                            }
                            else{
                                Query query = collectionReference.whereEqualTo("noteTitle",s.toString());
                                FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                                        .setQuery(query, NotesClass.class)
                                        .build();
                                notesAdapter.updateOptions(options);
                                notesAdapter.startListening();

                            }


                        }




                    }
                }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }
        });
        notes_recyclerview = findViewById(R.id.notesRecyclerView_ht);
        notes_recyclerview.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(NUM_COLUMN, LinearLayoutManager.VERTICAL);
        notes_recyclerview.setLayoutManager(layoutManager);







        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){
                    collectionReference =  firestore.collection("users").document(userID)
                            .collection("All Files").document(HT)
                            .collection("Courses").document(title)
                            .collection("Batches").document(sec).collection("MyNotes");

                    Query query = collectionReference;
                    FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                            .setQuery(query, NotesClass.class)
                            .build();
                    notesAdapter = new NotesHomeTutorAdapter(options);

                    notesAdapter.startListening();
                    notesAdapter.setTitle(title);

                    notesAdapter.setSection(sec);


                    notes_recyclerview.setAdapter(notesAdapter);

                    CollectionReference documentReference =  firestore.collection("users").document(userID)
                            .collection("All Files").document(HT).collection("Courses")
                            .document(title).collection("Batches").document(sec).collection("MyNotes");
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<NotesClass> documentData = queryDocumentSnapshots.toObjects(NotesClass.class);

                            notesClassList.addAll(documentData);

                        }
                    });

                    notesAdapter.notifyDataSetChanged();



                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    collectionReference =  firestore.collection("users").document(userID)
                            .collection("Courses").document(title)
                            .collection("Batches").document(sec).collection("MyNotes");

                    Query query = collectionReference;
                    FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                            .setQuery(query, NotesClass.class)
                            .build();
                    notesAdapter = new NotesHomeTutorAdapter(options);
                    notesAdapter.startListening();
                    notes_recyclerview.setAdapter(notesAdapter);


                    DocumentReference documentReference = firestore.collection("users").document(userID)
                            .collection("Courses").document(title)
                            .collection("Batches").document(sec).collection("MyNotes").document(getNote_title());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            NotesClass notesClass = documentSnapshot.toObject(NotesClass.class);

                            notesClassList.add(new NotesClass( notesClass.getNoteTitle(),notesClass.getSubtitle(),notesClass.getDate(),notesClass.getMynote(),notesClass.getUrl()));



                        }
                    });
                    notesAdapter.notifyDataSetChanged();


                }




            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });






        ImageView imageAddNoteMain = findViewById(R.id.addnotes_hometutor);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent1 = new Intent(getApplicationContext(),CreateNoteHometutor.class);
                intent1.putExtra("section",sec);
                intent1.putExtra("title",title);
                startActivity(intent1);

            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(notes_recyclerview);


    }
    private void searchNotes(String toLowerCase) {
        CollectionReference collectionReference =  firestore.collection("users").document(userID)
                .collection("Courses").document(title)
                .collection("Batches").document(sec).collection("MyNotes");
        Query query = collectionReference.orderBy("search").startAt(toLowerCase).endAt(toLowerCase+"\uf0ff");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                notesClassList.clear();

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    NotesClass notesClass = documentSnapshot.toObject(NotesClass.class);
                    assert notesClass!=null;
                    assert firebaseUser!=null;
                    if(notesClass.getNoteTitle()!=null)// && notesClass.getNoteTitle().equals())
                    {
                        notesClassList.add(notesClass);
                        System.out.println(notesClass);
                        System.out.println("hello" + notesClass.getNoteTitle());

                    }
                    else
                    {

                        System.out.println("edrftgyhujikl");
                    }
               }


                notes_recyclerview.setAdapter(notesAdapter);
                notesAdapter.notifyDataSetChanged();


            }

        });
    }
    @Override
    public void onStart() {
        super.onStart();

    }
    @Override
    public void onStop() {
        super.onStop();
        notesAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){

                    collectionReference = firestore.collection("users").document(userID)
                            .collection("All Files").document(HT)
                            .collection("Courses").document(title)
                            .collection("Batches").document(sec).collection("MyNotes");

                    Query query = collectionReference;
                    FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                            .setQuery(query, NotesClass.class)
                            .build();
                    notesAdapter = new NotesHomeTutorAdapter(options);
                    notesAdapter.startListening();
                    notesAdapter.setTitle(title);

                    notesAdapter.setSection(sec);


                    notes_recyclerview.setAdapter(notesAdapter);

                    CollectionReference documentReference =  firestore.collection("users").document(userID)
                            .collection("All Files").document(HT).collection("Courses")
                            .document(title).collection("Batches").document(sec).collection("MyNotes");
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<NotesClass> documentData = queryDocumentSnapshots.toObjects(NotesClass.class);

                            notesClassList.addAll(documentData);

                        }
                    });

                    notesAdapter.notifyDataSetChanged();



                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    collectionReference =  firestore.collection("users").document(userID)
                            .collection("Courses").document(title)
                            .collection("Batches").document(sec).collection("MyNotes");
                    Query query = collectionReference;
                    FirestoreRecyclerOptions<NotesClass> options = new FirestoreRecyclerOptions.Builder<NotesClass>()
                            .setQuery(query, NotesClass.class)
                            .build();
                    notesAdapter = new NotesHomeTutorAdapter(options);
                    notesAdapter.startListening();
                    notes_recyclerview.setAdapter(notesAdapter);
                    // classitems.addAll(options);

                    DocumentReference documentReference = firestore.collection("users").document(userID)
                            .collection("Courses").document(title)
                            .collection("Batches").document(sec).collection("MyNotes").document(getNote_title());
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            NotesClass notesClass = documentSnapshot.toObject(NotesClass.class);

                            notesClassList.add(new NotesClass( notesClass.getNoteTitle(),notesClass.getSubtitle(),notesClass.getDate(),notesClass.getMynote(),notesClass.getUrl()));



                        }
                    });
                    notesAdapter.notifyDataSetChanged();


                }




            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


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
                Toast.makeText(getApplicationContext(), "Deleting", Toast.LENGTH_LONG).show();
                notesAdapter.deleteItem(viewHolder.getAdapterPosition());

            }



        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}