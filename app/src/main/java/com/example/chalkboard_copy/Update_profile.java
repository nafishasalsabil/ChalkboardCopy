package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Update_profile extends AppCompatActivity {

    private EditText username, email, password;
    private Button update;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    public DatabaseReference usernameref;
    FirebaseUser firebaseUser;
    public String un = "";
    public String em = "";
    public String pw = "";
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();

    private DocumentReference documentReference = firestore.collection("users").document(userID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        update = findViewById(R.id.update);
     //   firebaseAuth = FirebaseAuth.getInstance();
     //   firestore = FirebaseFirestore.getInstance();
    //    userID = firebaseAuth.getCurrentUser().getUid();
      //  documentReference = firestore.collection("users").document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    un = documentSnapshot.getString("username");
                    em = documentSnapshot.getString("email");
                    pw = documentSnapshot.getString("password");
                    username.setText(un);
                    password.setText(pw);

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

       /* firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
         usernameref = databaseReference.child("users").child(firebaseAuth.getUid());
        usernameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //   ForSignupDatabase forSignupDatabase = snapshot.getValue(ForSignupDatabase.class);
                     un = dataSnapshot.child("username").getValue().toString();
                     em = dataSnapshot.child("email").getValue().toString();
                     pw = dataSnapshot.child("password").getValue().toString();
                    username.setText(un);
                    email.setText(em);
                    password.setText(pw);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                startActivity(new Intent(getApplicationContext(), Features.class));
            }
        });


    }

    private void update() {
        if (isNameChanged() && isPassChanged()
                || isNameChanged()|| isPassChanged() ) {
            Toast.makeText(this, "Your profile has been updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPassChanged() {
        if (!pw.equals(password.getText().toString())) {
          //  usernameref.child("password").setValue(password.getText().toString());
            String changepass = password.getText().toString();
            documentReference.update("password",changepass);
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        if (!un.equals(username.getText().toString())) {
          //  usernameref.child("username").setValue(username.getText().toString());

            String changename = username.getText().toString();
            documentReference.update("username",changename);
            return true;
        } else {
            return false;
        }

    }




}
