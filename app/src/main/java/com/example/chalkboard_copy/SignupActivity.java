package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText username;
    private EditText password;
    private EditText confirmpassword;
    private Button signup;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID ="";
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = (EditText)findViewById(R.id.usernameedittext);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.passwordedittext);
        confirmpassword = (EditText)findViewById(R.id.confirmpassedittext);
        signup = (Button)findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
             //   FirebaseDatabase.getInstance().setPersistenceEnabled(false);

               // firebaseDatabase = FirebaseDatabase.getInstance();
          //      databaseReference = firebaseDatabase.getReference("users");
                final String mail = email.getText().toString().trim();
                String usern =    username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String conf = confirmpassword.getText().toString().trim();

             //   String choice = "";
            //    Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("username").equalTo(user);
           /*     usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            username.setError("Username is already taken!");
                            i=1;
                            progressBar.setVisibility(View.GONE);

                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

                if (TextUtils.isEmpty(usern)) {
                    username.setError("Username is required");
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email is required");
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is required");
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                if (TextUtils.isEmpty(conf)) {
                    confirmpassword.setError("Confirm the password");
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                if(pass.length()<6)
                {
                    password.setError("Password must be of more than 6 characters");
                    progressBar.setVisibility(View.GONE);

                    return;
                }

                if(pass.equals(conf) && i==0 )
                {
                    firebaseAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().sendEmailVerification().
                                        addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                           userID =  firebaseAuth.getCurrentUser().getUid();
                                            DocumentReference documentReference = firestore.collection("users").document(userID);
                                            Map<String,Object> user = new HashMap<>();
                                            user.put("username",usern);
                                            user.put("email",mail);
                                            user.put("password",pass);
                                            user.put("id",userID);
                                            user.put("active_status","offline");
                                            user.put("search",usern.toLowerCase());
                                            user.put("imageUrl","not_selected");
                                            user.put("choice","not_selected");


                                         /*   ChatUser chatUser = new ChatUser();
                                            chatUser.setName(usern);
                                            chatUser.setImageUrl("default");
                                            chatUser.setId(userID);
                                         */
                                            System.out.println(userID);
                                         ForSignupDatabase forSignupDatabase = new ForSignupDatabase();
                                         forSignupDatabase.setId(userID);
                                         documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    startActivity(new Intent(getApplicationContext(), Main2Activity.class));

                                                    Toast.makeText(SignupActivity.this, "Signing up completed!Please check your email for verification and sign in.", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        //    ForSignupDatabase forSignupDatabase = new ForSignupDatabase(user,mail,pass,choice);
                                       /*   FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                   .setValue(forSignupDatabase).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   startActivity(new Intent(getApplicationContext(), Main2Activity.class));

                                                   Toast.makeText(SignupActivity.this, "Signing up completed!Please check your email for verification and sign in.", Toast.LENGTH_SHORT).show();

                                               }
                                           });*/


                                        }


                                    }
                                });

                        } else {
                            if(task.getException() instanceof FirebaseAuthInvalidUserException)
                            {
                                Toast.makeText(SignupActivity.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                            {
                                Toast.makeText(SignupActivity.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        // ...
                    }
                });
                }
            }
        });

    }


    public void signin(View view) {

        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
    }
}

