package com.example.chalkboard_copy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ClassesFragment_HomeTutor extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    TextView t1, t2;
    private RecyclerView recyclerView;
    private HomeTutorClassAdapter homeTutorClassAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<CourseInfoHomeTutorClass> courseInfoHomeTutorClassList = new ArrayList<>();
    CollectionReference collectionReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classes_hometutor,container, false);
        t1 = (TextView) view.findViewById(R.id.t1_ht);
        t2 = (TextView) view.findViewById(R.id.t2_ht);
        recyclerView = (RecyclerView) view.findViewById(R.id.courserecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        collectionReference = firestore.collection("users").document(userID).collection("Courses");
        //  List<CourseInfo> documentData = queryDocumentSnapshots.toObjects(CourseInfo.class);
        Query query = collectionReference;
        FirestoreRecyclerOptions<CourseInfoHomeTutorClass> options = new FirestoreRecyclerOptions.Builder<CourseInfoHomeTutorClass>()
                .setQuery(query, CourseInfoHomeTutorClass.class)
                .build();
        homeTutorClassAdapter = new HomeTutorClassAdapter(options);
        recyclerView.setAdapter(homeTutorClassAdapter);
        // classitems.addAll(options);
        homeTutorClassAdapter.notifyDataSetChanged();
        t1.setVisibility(View.GONE);
        t2.setVisibility(View.GONE);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        /*if (homeTutorClassAdapter.getItemCount() == 0) {

            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);

        }
        else {
            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);

        }*/
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.addclass_hometutor);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.add_classes_dialogbox_hometutor, null);
                alerDialog2.setView(view);
                AlertDialog dialog = alerDialog2.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
                EditText courseName = view.findViewById(R.id.coursename_hometutor_edittext);
                EditText className = view.findViewById(R.id.class_hometutor_edittext);

                Button add_button = (Button) view.findViewById(R.id.hometutor_class_add_button);
                Button cancel_button = (Button) view.findViewById(R.id.hometutor_class_cancel_button);

                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String coursename = courseName.getText().toString();
                        String classname = className.getText().toString();
                        if (TextUtils.isEmpty(coursename))
                        {
                            courseName.setError("required");
                            return;
                        }
                        if (TextUtils.isEmpty(classname))
                        {
                            className.setError("required");
                            return;
                        }
                        DocumentReference documentReference = firestore.collection("users").document(userID).collection("Courses").document(coursename);
                       CourseInfoHomeTutorClass courseInfoHomeTutorClass = new CourseInfoHomeTutorClass(coursename,classname);
                        documentReference.set(courseInfoHomeTutorClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "The course is added!", Toast.LENGTH_SHORT).show();

                            }
                        });
                        dialog.dismiss();


                    }
                });
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        homeTutorClassAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        homeTutorClassAdapter.stopListening();
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
                Toast.makeText(getContext(), "Deleting", Toast.LENGTH_LONG).show();
                homeTutorClassAdapter.deleteItem(viewHolder.getAdapterPosition());

            }

           /* int position = viewHolder.getAdapterPosition();
            switch(direction)
            {
                case ItemTouchHelper.LEFT:
                    String course_name = classitems.get(position).getCourseTitle();
                    archive.add(course_name);
                    classitems.remove(position);
                    classAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView,course_name+" Archived", Snackbar.LENGTH_LONG)
                           *//* .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    archive.remove(archive.lastIndexOf(course_name));
                                    classitems.;
                                }
                            }).show()*//*;


            }
*/

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.absent))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

}
