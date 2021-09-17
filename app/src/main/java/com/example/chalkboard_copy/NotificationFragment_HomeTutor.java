package com.example.chalkboard_copy;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationFragment_HomeTutor extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    List<ForSignupDatabase> forSignupDatabaseList = new ArrayList<>();
    private RecyclerView recyclerView;
    NoticeboardAdapter noteceboardAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<NoticeClass> noticeClassList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications_hometutor,container, false);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.add_notice);
        recyclerView = view.findViewById(R.id.notice_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        CollectionReference collectionReference3 = firestore.collection("Notices");
collectionReference3.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
    @Override
    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
        noticeClassList.clear();
        List<NoticeClass> doc = queryDocumentSnapshots.toObjects(NoticeClass.class);
        noteceboardAdapter = new NoticeboardAdapter(getContext(), noticeClassList);
        recyclerView.setAdapter(noteceboardAdapter);
        noticeClassList.addAll(doc);
        noteceboardAdapter.notifyDataSetChanged();


    }
});


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(getContext());
                View v = LayoutInflater.from(getContext()).inflate(R.layout.add_post_dialogbox, null);
                alerDialog2.setView(v);
                AlertDialog dialog = alerDialog2.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

                Button post = (Button) v.findViewById(R.id.Post_UploadBtn);
             //   Button no = (Button) view.findViewById(R.id.no_button);Not
              TextView titleEt = v.findViewById(R.id.PostTittleET);
              TextView  descriptionEt = v.findViewById(R.id.PostDescriptionET);

                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = titleEt.getText().toString().trim();
                        String description = descriptionEt.getText().toString().trim();
                        Date date = new Date();
                        String stringDate = DateFormat.getDateTimeInstance().format(date);
                        System.out.println(stringDate);

                        if(TextUtils.isEmpty(title))
                        {
                            Toast.makeText(getContext(),"Enter Title",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(TextUtils.isEmpty(description))
                        {
                            Toast.makeText(getContext(),"Enter Description",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DocumentReference documentReference1 = firestore.collection("Notices").document(title);
                        Map<String, Object> inuser = new HashMap<>();
                        inuser.put("title",title );
                        inuser.put("description",description);

                        DocumentReference documentReference = firestore.collection("users").document(userID);
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ForSignupDatabase forSignupDatabase = documentSnapshot.toObject(ForSignupDatabase.class);
                                System.out.println(forSignupDatabase.getId());
                                String id = forSignupDatabase.getId();
                                System.out.println(forSignupDatabase.getUsername());
                                System.out.println(forSignupDatabase.getImageUrl());

                                inuser.put("id",forSignupDatabase.getId());
                                inuser.put("name",forSignupDatabase.getUsername());
                                inuser.put("imageUrl",forSignupDatabase.getImageUrl());
                                inuser.put("posted_at",stringDate);
                                inuser.put("like",0);
                                documentReference1.set(inuser);
                                   NoticeClass noticeClass = new NoticeClass(title,description,forSignupDatabase.getUsername(),stringDate,forSignupDatabase.getId(),forSignupDatabase.getImageUrl(),0);
                                noticeClassList.add(noticeClass);
                                recyclerView.setAdapter(noteceboardAdapter);
                                noteceboardAdapter.notifyDataSetChanged();


                            }
                        });


                        dialog.dismiss();

                    }
                });



            }
        });
        return view;
    }
    private void uploadData(String title, String description){
       /* pd.setMessage("Publishing post");
        pd.show();
*/
        //for post-image name, post-id,post publish time

        String timestamp = String.valueOf(System.currentTimeMillis());

        //String filePathName = "Posts/" + "post_" + timestamp;

        //Data Base
    }

}
