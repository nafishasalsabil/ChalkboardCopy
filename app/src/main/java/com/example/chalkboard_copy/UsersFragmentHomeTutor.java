package com.example.chalkboard_copy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class UsersFragmentHomeTutor extends Fragment {
    RecyclerView users_recycler_view;
    ChatUserHomeTutorAdapter chatUserAdapter;
    List<ChatUser> chatUsers;
    EditText search_user;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    String userID = firebaseAuth.getCurrentUser().getUid();


    public UsersFragmentHomeTutor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_home_tutor, container, false);
        search_user = view.findViewById(R.id.search_users_ht);
        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        users_recycler_view = view.findViewById(R.id.users_recycler_view_ht);
        users_recycler_view.setHasFixedSize(true);
        users_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        chatUsers = new ArrayList<>();
        readUsers();

        return view;
    }
    private void searchUsers(String toString) {
        CollectionReference collectionReference = firestore.collection("users");
        Query query = collectionReference.orderBy("search").startAt(toString).endAt(toString+"\uf0ff");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                chatUsers.clear();

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ChatUser chatUser = documentSnapshot.toObject(ChatUser.class);
                    assert chatUser!=null;
                    assert firebaseUser!=null;
                    if(chatUser.getId()!=null && !(chatUser.getId().equals(firebaseUser.getUid())) )
                    {
//                        && chatUser.getChoice().equals("Home tutor")
                        chatUsers.add(chatUser);
                        System.out.println(chatUsers);
                        System.out.println("hello" + chatUser.getId());
                        System.out.println(chatUser.getChoice());

                    }
                    else
                    {

                        System.out.println("edrftgyhujikl");
                    }
              }

                chatUserAdapter = new ChatUserHomeTutorAdapter(getContext(),chatUsers,false);
                users_recycler_view.setAdapter(chatUserAdapter);

            }

        });
    }

    private void readUsers() {

        CollectionReference collectionReference = firestore.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (search_user.getText().toString().equals("")) {

                    chatUsers.clear();

                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ChatUser chatUser = documentSnapshot.toObject(ChatUser.class);
                        if (chatUser.getId() != null && !(chatUser.getId().equals(firebaseUser.getUid())) ) {

                                chatUsers.add(chatUser);
                                System.out.println(chatUsers);
                                System.out.println("hello" + chatUser.getId());
                                chatUserAdapter = new ChatUserHomeTutorAdapter(getContext(), chatUsers, false);
                                users_recycler_view.setAdapter(chatUserAdapter);
                                chatUserAdapter.notifyDataSetChanged();


                        } else {

                        }

                    }
                }
            }

            });

        }

    }
