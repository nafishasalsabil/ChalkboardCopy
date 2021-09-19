package com.example.chalkboard_copy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public  class Main2Activity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private Button signin;
    private TextView username;
    private EditText email;
    private EditText password;
    private TextView signup;
    private TextView forgot;
    private TextView google;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 1;
    ProgressBar progressBar;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private GoogleApiClient googleApiClient;
SharedPreferences sharedPreferences;

Boolean normal_signin = false;

  /*  @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser =mAuth.getCurrentUser();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(this,Features.class);

            startActivity(intent);

        }
        *//*else
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

        }
*//*
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        forgot = findViewById(R.id.forgotpass);
        forgot.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1074227662938-f8qntntva3hr0svt00gt9f4vajquotpq.apps.googleusercontent.com")
                .requestEmail()
                .build();
     /*   googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
*/
// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signin = (Button)findViewById(R.id.signin);
        email = (EditText)findViewById(R.id.email);
        password =(EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressbar);
        google = (TextView)findViewById(R.id.singup_with_google);




  //      firestore = FirebaseFirestore.getInstance();

       /*google = findViewById(R.id.google);
/       google.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                signIn();
            }
        });*/
        signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                normal_signin = true;
                progressBar.setVisibility(View.VISIBLE);
                 String mail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();


                if (TextUtils.isEmpty(mail)) {
                    email.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    password.setError("Password is required");
                    return;
                }

                if(pass.length()<6)
                {
                    password.setError("Password must be of more than 6 characters");
                    return;
                }
                mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                               /* sharedPreferences = getSharedPreferences("selected", Context.MODE_PRIVATE);
                                boolean bool= sharedPreferences.getBoolean("lockedState", true);
                              *//*  if(bool == true)
                                {
                                    System.out.println("helloooooooooooooooooooooooo");
                                    startActivity(new Intent(getApplicationContext(), Features.class));

                                }
                                else
                                {*/
                                String userID = firebaseAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = firestore.collection("users").document(userID);
                                  documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                      @Override
                                      public void onSuccess(DocumentSnapshot documentSnapshot) {
                                          UserClass userClass = documentSnapshot.toObject(UserClass.class);
                                          userClass.getChoice();
                                          if(userClass.getChoice().equals("Professional teacher"))
                                          {
                                              startActivity(new Intent(getApplicationContext(), Features.class));
                                              Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                          }
                                          else if(userClass.getChoice().equals("Home tutor"))
                                          {
                                              startActivity(new Intent(getApplicationContext(), MainActivity_HomeTutor.class));
                                              Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                          }
                                          else if(userClass.getChoice().equals("Professional teacher / Home tutor"))
                                          {
                                              startActivity(new Intent(getApplicationContext(), Features.class));
                                              Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                          }
                                          else
                                          {
                                              Intent intent = new Intent(getApplicationContext(),ChoiceActivity.class);
                                              intent.putExtra("Normal_signin",true);
                                              startActivity(intent);
                                              Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                          }
                                      }
                                  });

                      //          }


                            }
                            else
                            {
                                Toast.makeText(Main2Activity.this, "Please verify your email", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else {
                            Toast.makeText(Main2Activity.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            normal_signin = false;

                        }
                    }
                });

            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                normal_signin = false;
                Intent signInIntent =mGoogleSignInClient.getSignInIntent();
                System.out.println("Check clicked?");
                resultLauncher.launch(signInIntent);

            }
        });

    }
    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK)
            {
                System.out.println("GVVHHHBBBBBBBBBBBBBBBBBBBBBBBBB");
                Intent intent = result.getData();
                Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(intent);

                if(signInAccountTask.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Google sign in complete!",Toast.LENGTH_SHORT).show();

                    try {
                        GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);

                        if(googleSignInAccount!=null){
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);

                            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){

                                        String userID = firebaseAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference1 = firestore.collection("users").document(userID);
                                        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                UserClass userClass = documentSnapshot.toObject(UserClass.class);
                                                userClass.getChoice();
                                                if(userClass.getChoice().equals("Professional teacher"))
                                                {
                                                    startActivity(new Intent(getApplicationContext(), Features.class));
                                                    Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                                }
                                                else if(userClass.getChoice().equals("Home tutor"))
                                                {
                                                    startActivity(new Intent(getApplicationContext(), MainActivity_HomeTutor.class));
                                                    Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                                }
                                                else if(userClass.getChoice().equals("Professional teacher / Home tutor"))
                                                {
                                                    Intent intent = new Intent(getApplicationContext(),Features.class);
                                                    /*intent.putExtra("Normal_signin",false);*/
                                                    startActivity(intent);
                                                    Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });


                                    }
                                }
                            });
                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                System.out.println("NOT OK");
                System.out.println(result.getResultCode());
            }
        }
    });

   /* private void signIn() {


        Intent signInIntent =mGoogleSignInClient.getSignInIntent();
        
        this.startActivityForResult(signInIntent, 100);
    }*/
    





   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            *//*GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);*//*

            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            if(signInAccountTask.isSuccessful()){
                Toast.makeText(getApplicationContext(),"Google sign in complete!",Toast.LENGTH_SHORT).show();

                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);

                    if(googleSignInAccount!=null){
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    String userID = firebaseAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference1 = firestore.collection("users").document(userID);
                                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            UserClass userClass = documentSnapshot.toObject(UserClass.class);
                                            userClass.getChoice();
                                            if(userClass.getChoice().equals("Professional teacher"))
                                            {
                                                startActivity(new Intent(getApplicationContext(), Features.class));
                                                Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                            }
                                            else if(userClass.getChoice().equals("Home tutor"))
                                            {
                                                startActivity(new Intent(getApplicationContext(), MainActivity_HomeTutor.class));
                                                Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                Intent intent = new Intent(getApplicationContext(),ChoiceActivity.class);
                                                intent.putExtra("Normal_signin",false);
                                                startActivity(intent);
                                                Toast.makeText(Main2Activity.this, "Sign in successful!", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });


                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        }
    }
*/
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile() {
        Intent intent=new Intent(Main2Activity.this,Features.class);
        startActivity(intent);

    }



    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
    }

    public void forgotpassword(View view) {
        startActivity(new Intent(getApplicationContext(),ForgotPassActivity.class));

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
