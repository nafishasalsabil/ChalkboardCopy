package com.example.chalkboard_copy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chalkboard_copy.Notifications.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MessagesFragmentHometutor extends Fragment {

    RecyclerView recyclerView_messages_fragment;
    ChatUserHomeTutorAdapter chatUserAdapter;
    List<ChatUser> chatUserList = new ArrayList<>();
    Set<String> userlistSet = new HashSet<>();
    List<String> userlist = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages_hometutor, container, false);
        recyclerView_messages_fragment = view.findViewById(R.id.messages_fragment_recyclerview_ht);
        recyclerView_messages_fragment.setHasFixedSize(true);
        recyclerView_messages_fragment.setLayoutManager(new LinearLayoutManager(getContext()));

        CollectionReference collectionReference1 = firestore.collection("home_tutor_chats");
        collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                userlistSet.clear();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    ChatRecordClass object = documentSnapshot.toObject(ChatRecordClass.class);
                    if (object.getSender().equals(userID)) {
                        userlistSet.add(object.getReciever());
                    }
                    if (object.getReciever().equals(userID)) {
                        userlistSet.add(object.getSender());
                    }



                }
                readChats();


           /*    classAdapter = new ClassAdapter(getApplicationContext(), classitems);
               List<CourseInfo> documentData = queryDocumentSnapshots.toObjects(CourseInfo.class);
*/
            }

        });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return  view;
    }
    private void readChats() {

        CollectionReference collectionReference = firestore.collection("users");

        userlist = convertSetToList(userlistSet);
        for(String data: userlistSet)
        {
            Log.d("checked",data);
        }

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ChatUser> data = queryDocumentSnapshots.toObjects(ChatUser.class);

                for (ChatUser chatUser : data) {
                    if (userlistSet.contains(chatUser.getId())) {
                        chatUserList.add(chatUser);
                    }

                }
                chatUserAdapter = new ChatUserHomeTutorAdapter(getContext(), chatUserList,true);
                recyclerView_messages_fragment.setAdapter(chatUserAdapter);
                chatUserAdapter.notifyDataSetChanged();


            }
        });




    }
    private void updateToken(String token)
    {
        DocumentReference documentReference = firestore.collection("Tokens").document(userID);
        Token token1 = new Token(token);
        documentReference.set(token1);
    }

    public static <T> List<T> convertSetToList(Set<T> set) {

        List<T> list = new ArrayList<>();


        for (T t : set)
            list.add(t);


        return list;
    }

}