package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ClassesFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    Toolbar toolbar_features;
    FirebaseUser firebaseUser;
    Button signout;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private NavController navController;
    BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView profilepic;
    public TextView username;
    public DatabaseReference databaseReference;
    public String u = "";


    TextView t1, t2;
    private RecyclerView recyclerView;
    private ClassAdapter classAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<CourseInfo> classitems = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    //    String name = getArguments().getString("data");
    EditText ct, cn;
    String title = "";
    // Toolbar classes_toolbar;
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";

    private DocumentReference documentReference;
    private CollectionReference collectionReference;

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_classes, container, false);

        t1 = (TextView) view.findViewById(R.id.t1);
        t2 = (TextView) view.findViewById(R.id.t2);
        ct = (EditText) view.findViewById(R.id.coursetitle);
        cn = (EditText) view.findViewById(R.id.courseno);
//        drawerLayout = view.findViewById(R.id.classes_dlayout)
        recyclerView = (RecyclerView) view.findViewById(R.id.classesrecyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();
                System.out.println(status);
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        if(status.equals("Professional teacher / Home tutor")){

            collectionReference = firestore.collection("users")
                    .document(userID).collection("All Files").document(PROF).collection("Courses");

        }
        else{

            collectionReference = firestore.collection("users").document(userID).collection("Courses");
        }




        Query query = collectionReference;
        FirestoreRecyclerOptions<CourseInfo> options = new FirestoreRecyclerOptions.Builder<CourseInfo>()
                .setQuery(query, CourseInfo.class)
                .build();
        classAdapter = new ClassAdapter(options);
        recyclerView.setAdapter(classAdapter);
       // classitems.addAll(options);



            t1.setVisibility(View.GONE);
            t2.setVisibility(View.GONE);



        classAdapter.notifyDataSetChanged();



      /*  collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<CourseInfo> documentData = queryDocumentSnapshots.toObjects(CourseInfo.class);
                classAdapter = new ClassAdapter(classitems);
                recyclerView.setAdapter(classAdapter);
                classitems.addAll(documentData);
                classAdapter.notifyDataSetChanged();
                t1.setVisibility(View.GONE);
                t2.setVisibility(View.GONE);
                //    System.out.println();
               *//* classAdapter.setOnItemClickListener(new ClassAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        gotoinsideclass(position);
                    }
                });*//*


            }
        });
*/
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.addclass);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewClassActivity.class));
            }
        });
     /*   drawerLayout = view.findViewById(R.id.classes_dlayout);
        navigationView = view.findViewById(R.id.navigationView);
     */
        DocumentReference id_documentReference = firestore.collection("users").document(userID);
       /* username = navigationView.getHeaderView(0).findViewById(R.id.username);
        profilepic = navigationView.getHeaderView(0).findViewById(R.id.profilepic);
       *//* actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, classes_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);*/
//        toolbar.setNavigationIcon(R.drawable.ic_person_outline_black_24dp);

   /*     drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) getActivity());
       */

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        classAdapter.startListening();
        String activity_name = getClass().getSimpleName();
        System.out.println(activity_name);
    }
    @Override
    public void onStop() {
        super.onStop();
        classAdapter.stopListening();
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
                Toast.makeText(getContext(), "Deleting", Toast.LENGTH_LONG).show();
                classAdapter.deleteItem(viewHolder.getAdapterPosition());

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
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    public void test() {

    }

    private void gotoinsideclass(int position) {
        startActivity(new Intent(getActivity(), Sections_Inside_Courses.class));

    }


    public ClassesFragment(String data) {
        this.title = data;
    }

    public ClassesFragment() {

    }

    public static ClassesFragment newInstance() {
        ClassesFragment fragment = new ClassesFragment();


        return fragment;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
              /*  Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()) {
                                    //gotoMainActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
*/

                break;
        }
        switch (menuItem.getItemId()) {
            case R.id.update_profile:
                startActivity(new Intent(getActivity(), Update_profile.class));
                break;
        }
        return true;
    }


}
