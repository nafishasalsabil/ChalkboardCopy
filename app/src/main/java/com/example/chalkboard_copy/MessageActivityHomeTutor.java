package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chalkboard_copy.Notifications.APIService;
import com.example.chalkboard_copy.Notifications.Client;
import com.example.chalkboard_copy.Notifications.Data;
import com.example.chalkboard_copy.Notifications.MyResponse;
import com.example.chalkboard_copy.Notifications.Sender;
import com.example.chalkboard_copy.Notifications.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivityHomeTutor extends AppCompatActivity {
    TextView username_textview;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    EditText message_edittext;
    ImageButton send_button;
    MessageHomeTutorAdapter messageAdapter;
    List<ChatRecordClass> chatRecordClasses = new ArrayList<>();
    RecyclerView messages_recyclerview;
    LinearLayoutManager linearLayoutManager;
    public String reciever = "";
    public String ts = "";
    CircleImageView profile;
    ValueEventListener seenListener;
    boolean isSeen = false;
    boolean notify = false;
    APIService apiService;
    DocumentReference documentReference12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_home_tutor);
        username_textview = findViewById(R.id.username_textview_ht);
        Toolbar toolbar_messages = findViewById(R.id.toolbar_messages_ht);
        message_edittext = findViewById(R.id.meassage_edit_text_ht);
        send_button = findViewById(R.id.button_send_ht);
        messages_recyclerview = findViewById(R.id.messages_recyclerview_ht);
        messages_recyclerview.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        //linearLayoutManager.setReverseLayout(true);
        messages_recyclerview.setLayoutManager(linearLayoutManager);


        setSupportActionBar(toolbar_messages);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_messages.setNavigationIcon(R.drawable.ic_back);

        profile = toolbar_messages.findViewById(R.id.profile_pic_ht);
        // toolbar_messages.set
        getSupportActionBar().setElevation(0);
        toolbar_messages.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        Intent intent = getIntent();
        String un = intent.getStringExtra("username");
        String passed_u_id = intent.getStringExtra("uid");
        System.out.println(passed_u_id);
        CollectionReference collectionReference = firestore.collection("users");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ChatUser chatUser = documentSnapshot.toObject(ChatUser.class);

                    if(chatUser.getUsername().equals(un))
                    {
                        username_textview.setText(chatUser.getUsername());
                        //  reciever = chatUser.getUsername();
                        readMessage(userID,passed_u_id);
                    }
                }

            }

        });
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = message_edittext.getText().toString();
                if(TextUtils.isEmpty(message))
                {
                    message_edittext.setError("You can't send empty message!");
                }
                else
                {
                    ts = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    System.out.println(ts);
                    Log.d("checktime",ts);
                    sendMessage(userID,passed_u_id,message,ts,isSeen);


                }
                message_edittext.setText("");
            }
        });

        seenMessage(passed_u_id);

    }
    private void seenMessage(String userId)
    {
        CollectionReference collectionReference2 = firestore.collection("home_tutor_chats");
        collectionReference2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ChatRecordClass object = documentSnapshot.toObject(ChatRecordClass.class);
                    if(object.getReciever().equals(userID) && object.getSender().equals(userId))
                    {

                        Map<String,Object> user = new HashMap<>();
                        user.put("isseen",true);
                        String uid = documentSnapshot.getId();

                        collectionReference2.document(uid).update(user);

                    }

                }



           /*    classAdapter = new ClassAdapter(getApplicationContext(), classitems);
               List<CourseInfo> documentData = queryDocumentSnapshots.toObjects(CourseInfo.class);
*/
            }

        });

    }
    private void readMessage(String userID, String passed_u_id) {
        CollectionReference collectionReference1 = firestore.collection("home_tutor_chats");
        collectionReference1.orderBy("time", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                chatRecordClasses.clear();

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ChatRecordClass object = documentSnapshot.toObject(ChatRecordClass.class);
                    if(object.getReciever().equals(passed_u_id) && object.getSender().equals(userID))
                    {
                        chatRecordClasses.add(object);
                    }
                    if(object.getReciever().equals(userID) && object.getSender().equals(passed_u_id))
                    {
                        chatRecordClasses.add(object);
                    }
                    messageAdapter = new MessageHomeTutorAdapter(getApplicationContext(), chatRecordClasses);
                    messages_recyclerview.setAdapter(messageAdapter);
                    messageAdapter.notifyDataSetChanged();
                    for(int i = 0;i<chatRecordClasses.size();i++)
                    {
                        System.out.println(chatRecordClasses.get(i).toString());
                    }
                }



           /*    classAdapter = new ClassAdapter(getApplicationContext(), classitems);
               List<CourseInfo> documentData = queryDocumentSnapshots.toObjects(CourseInfo.class);
*/
            }

        });



/*
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<ChatRecordClass> documentData = queryDocumentSnapshots.toObjects(ChatRecordClass.class);
             //   ChatRecordClass object = queryDocumentSnapshots.toObjects(ChatRecordClass.class);
                if(object.getReciever().equals(userID) && object.getSender().equals(passed_u_id))
                {
                    chatRecordClasses.add(object);
                }
                messageAdapter = new MessageAdapter(getApplicationContext(), chatRecordClasses);
                messages_recyclerview.setAdapter(messageAdapter);
            }
*/}

    private void sendMessage(String userID, String passed_u_id, String message, String ts, boolean isSeen) {
        CollectionReference documentReference = firestore.collection("home_tutor_chats");
        ChatRecordClass chatRecordClass = new ChatRecordClass(userID,passed_u_id,message,ts,false);
        messageAdapter = new MessageHomeTutorAdapter(getApplicationContext(), chatRecordClasses);
        messages_recyclerview.setAdapter(messageAdapter);
        chatRecordClasses.add(chatRecordClass);
        messageAdapter.notifyDataSetChanged();
        documentReference.add(chatRecordClass);

        final String msg = message;
        DocumentReference documentReference1 = firestore.collection("users").document(userID);
        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ChatUser chatUser = documentSnapshot.toObject(ChatUser.class);
                if(notify)
                {
                    sendNotification(passed_u_id,chatUser.getUsername(),message);
                    System.out.println("SDRFGHJKM,LJHGFDFGHJK");

                }
                notify= false;
            }
        });

       /* DocumentReference listreference = firestore.collection("Chatlist").document(userID).collection(passed_u_id).document("list");
        listreference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        listreference.collection("id").add(passed_u_id);
                    }
                }
            }
        });*/
    }

    private void sendNotification(String passed_u_id, String username, String message) {
        CollectionReference collectionReference = firestore.collection("Tokens");
        Query query = collectionReference.orderBy(passed_u_id);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Token token = documentSnapshot.toObject(Token.class);
                    Data data = new Data(userID, R.drawable.ic_profile, username + " :" + message, "New message", passed_u_id);
                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotifications(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code()==200)
                                    {
                                        if(response.body().success==1)
                                        {
                                            Toast.makeText(getApplicationContext(),"Failed!",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }
        });
    }

    private void status(String status)
    {
        documentReference12 = firestore.collection("users").document(userID);
        Map<String,Object> user = new HashMap<>();
        user.put("active_status",status);
        documentReference12.set(user, SetOptions.merge());

    }

    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();

        status("offline");
    }
}