package com.example.chalkboard_copy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class Features extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    Toolbar toolbar_features;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button signout;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    private NavController navController;
    BottomNavigationView bottomNavigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ImageView profilepic;
    public TextView username;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public String u = "";
    private MenuItem mActiveBottomNavigationViewMenuItem;
    FirebaseFirestore firestore;
    String userID;
    String courseTitle="";
    Bitmap bitmap;
    Boolean normal_signin;
    String prof ="";
    public static String activity_name = "";
    public static String link_image ="";

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener;


    //  private static final String USER = " users";

 /*   @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();


        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);

    }
*/
 Target mTarget = new Target() {
     @Override
     public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
         System.out.println("onbitmaploaded");
         Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
         scaledBitmap = getCircularBitmap(scaledBitmap);
         BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), scaledBitmap);
         toolbar_features.setNavigationIcon(mBitmapDrawable);
     }

     @Override
     public void onBitmapFailed(Exception e, Drawable errorDrawable) {
         System.out.println("Failed loading image");
     }

     @Override
     public void onPrepareLoad(Drawable drawable) {
         System.out.println("prepare loading image");
     }
 };

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("-------------");
        firestore = FirebaseFirestore.getInstance();
        userID =  firebaseAuth.getCurrentUser().getUid();
        System.out.println(userID);
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();
                    ChatUser chatUsers = doc.toObject(ChatUser.class);
                    System.out.println(chatUsers.getImageUrl());
                    StringBuilder fields = new StringBuilder("");
                    u = fields.append(doc.get("username")).toString();
                    //  fields.append("\nEmail: ").append(doc.get("email"));
                    //   fields.append("\nPhone: ").append(doc.get("phone"));
                    username.setText(u);
                     link_image = chatUsers.getImageUrl();
                    System.out.println(link_image+"--------------imageStart");
                    if(!(link_image.equals("not_selected")))
                    {
                        Picasso.get().load(link_image).into(profilepic);
                      /*  Target mTarget = new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                                System.out.println("onbitmaploaded");
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                                scaledBitmap = getCircularBitmap(scaledBitmap);
                                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), scaledBitmap);
                                toolbar_features.setNavigationIcon(mBitmapDrawable);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                System.out.println("Failed loading image");
                            }

                            @Override
                            public void onPrepareLoad(Drawable drawable) {
                                System.out.println("prepare loading image");
                            }
                        };*/


                        Picasso.get().load(link_image).into(mTarget);
                       /* Picasso.get().load(link_image).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                                scaledBitmap = getCircularBitmap(scaledBitmap);
                                Drawable d = new BitmapDrawable(getResources(), scaledBitmap);
                                System.out.println("::::::::::::::::::::::::::::::::::::::::::::");
                                toolbar_features.setNavigationIcon(d);
                                //getSupportActionBar().setDisplayShowHomeEnabled(true);
                                // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });*/
                    }
                    else {



                        if (u.startsWith("a") || u.startsWith("A")) {
                            toolbar_features.setNavigationIcon(R.drawable.a_un);
                            profilepic.setImageResource(R.drawable.a_un);

                        }
                        if (u.startsWith("b") || u.startsWith("B")) {
                            toolbar_features.setNavigationIcon(R.drawable.b_letter);
                            profilepic.setImageResource(R.drawable.b_letter);

                        }
                        if (u.startsWith("c") || u.startsWith("C")) {
                            toolbar_features.setNavigationIcon(R.drawable.c);
                            profilepic.setImageResource(R.drawable.c);

                        }
                        if (u.startsWith("d") || u.startsWith("D")) {
                            toolbar_features.setNavigationIcon(R.drawable.d);
                            profilepic.setImageResource(R.drawable.d);
                            profilepic.buildDrawingCache();
                            bitmap = profilepic.getDrawingCache();

                        }
                        if (u.startsWith("e") || u.startsWith("E")) {
                            toolbar_features.setNavigationIcon(R.drawable.e);
                            profilepic.setImageResource(R.drawable.e);

                        }
                        if (u.startsWith("f") || u.startsWith("F")) {
                            toolbar_features.setNavigationIcon(R.drawable.f);
                            profilepic.setImageResource(R.drawable.f);

                        }
                        if (u.startsWith("g") || u.startsWith("G")) {
                            toolbar_features.setNavigationIcon(R.drawable.g);
                            profilepic.setImageResource(R.drawable.g);

                        }
                        if (u.startsWith("h") || u.startsWith("H")) {
                            toolbar_features.setNavigationIcon(R.drawable.h);
                            profilepic.setImageResource(R.drawable.h);

                        }
                        if (u.startsWith("i") || u.startsWith("I")) {
                            toolbar_features.setNavigationIcon(R.drawable.i);
                            profilepic.setImageResource(R.drawable.i);

                        }
                        if (u.startsWith("j") || u.startsWith("J")) {
                            toolbar_features.setNavigationIcon(R.drawable.j);
                            profilepic.setImageResource(R.drawable.j);

                        }
                        if (u.startsWith("k") || u.startsWith("K")) {
                            toolbar_features.setNavigationIcon(R.drawable.k);
                            profilepic.setImageResource(R.drawable.k);

                        }
                        if (u.startsWith("l") || u.startsWith("L")) {
                            toolbar_features.setNavigationIcon(R.drawable.l_un);
                            profilepic.setImageResource(R.drawable.l_un);

                        }
                        if (u.startsWith("m") || u.startsWith("M")) {
                            toolbar_features.setNavigationIcon(R.drawable.m);
                            profilepic.setImageResource(R.drawable.m);

                        }
                        if (u.startsWith("n") || u.startsWith("N")) {
                            toolbar_features.setNavigationIcon(R.drawable.ic_n);
                            profilepic.setImageResource(R.drawable.ic_n);

                        }
                        if (u.startsWith("o") || u.startsWith("O")) {
                            toolbar_features.setNavigationIcon(R.drawable.o);
                            profilepic.setImageResource(R.drawable.o);

                        }
                        if (u.startsWith("p") || u.startsWith("P")) {
                            toolbar_features.setNavigationIcon(R.drawable.p_un);
                            profilepic.setImageResource(R.drawable.p_un);

                        }
                        if (u.startsWith("q") || u.startsWith("Q")) {
                            toolbar_features.setNavigationIcon(R.drawable.q);
                            profilepic.setImageResource(R.drawable.q);

                        }
                        if (u.startsWith("r") || u.startsWith("R")) {
                            toolbar_features.setNavigationIcon(R.drawable.r);
                            profilepic.setImageResource(R.drawable.r);

                        }
                        if (u.startsWith("s") || u.startsWith("S")) {
                            toolbar_features.setNavigationIcon(R.drawable.s);
                            profilepic.setImageResource(R.drawable.s);

                        }
                        if (u.startsWith("t") || u.startsWith("T")) {
                            toolbar_features.setNavigationIcon(R.drawable.t);
                            profilepic.setImageResource(R.drawable.t);

                        }
                        if (u.startsWith("u") || u.startsWith("U")) {
                            toolbar_features.setNavigationIcon(R.drawable.u);
                            profilepic.setImageResource(R.drawable.u);

                        }
                        if (u.startsWith("v") || u.startsWith("V")) {
                            toolbar_features.setNavigationIcon(R.drawable.v);
                            profilepic.setImageResource(R.drawable.v);

                        }
                        if (u.startsWith("w") || u.startsWith("W")) {
                            toolbar_features.setNavigationIcon(R.drawable.w);
                            profilepic.setImageResource(R.drawable.w);

                        }
                        if (u.startsWith("x") || u.startsWith("X")) {
                            toolbar_features.setNavigationIcon(R.drawable.x);
                            profilepic.setImageResource(R.drawable.x);

                        }
                        if (u.startsWith("y") || u.startsWith("Y")) {
                            toolbar_features.setNavigationIcon(R.drawable.y);
                            profilepic.setImageResource(R.drawable.y);

                        }
                        if (u.startsWith("z") || u.startsWith("Z")) {
                            toolbar_features.setNavigationIcon(R.drawable.z);
                            profilepic.setImageResource(R.drawable.z);

                        }
                        if (u.startsWith("0")) {
                            toolbar_features.setNavigationIcon(R.drawable.no0);
                            profilepic.setImageResource(R.drawable.no0);

                        }
                        if (u.startsWith("1")) {
                            toolbar_features.setNavigationIcon(R.drawable.no1);
                            profilepic.setImageResource(R.drawable.no1);

                        }
                        if (u.startsWith("2")) {
                            toolbar_features.setNavigationIcon(R.drawable.no2);
                            profilepic.setImageResource(R.drawable.no2);

                        }
                        if (u.startsWith("3")) {
                            toolbar_features.setNavigationIcon(R.drawable.no3);
                            profilepic.setImageResource(R.drawable.no3);

                        }
                        if (u.startsWith("4")) {
                            toolbar_features.setNavigationIcon(R.drawable.no4);
                            profilepic.setImageResource(R.drawable.no4);

                        }
                        if (u.startsWith("5")) {
                            toolbar_features.setNavigationIcon(R.drawable.no5);
                            profilepic.setImageResource(R.drawable.no5);

                        }
                        if (u.startsWith("6")) {
                            toolbar_features.setNavigationIcon(R.drawable.no6);
                            profilepic.setImageResource(R.drawable.no6);

                        }
                        if (u.startsWith("8")) {
                            toolbar_features.setNavigationIcon(R.drawable.no8);
                            profilepic.setImageResource(R.drawable.no8);

                        }
                        if (u.startsWith("7")) {
                            toolbar_features.setNavigationIcon(R.drawable.no7);
                            profilepic.setImageResource(R.drawable.no7);

                        }
                        if (u.startsWith("9")) {
                            toolbar_features.setNavigationIcon(R.drawable.no9);
                            profilepic.setImageResource(R.drawable.no9);

                        }
                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

   /* @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }*/

   /* @Override
    protected void onResume() {
        super.onResume();
        System.out.println("+++++++++++");
        firestore = FirebaseFirestore.getInstance();
        userID =  firebaseAuth.getCurrentUser().getUid();
        System.out.println(userID);
        DocumentReference documentReference = firestore.collection("users").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot doc = task.getResult();
                    ChatUser chatUsers = doc.toObject(ChatUser.class);
                    System.out.println(chatUsers.getImageUrl());
                    StringBuilder fields = new StringBuilder("");
                    u = fields.append(doc.get("username")).toString();
                    //  fields.append("\nEmail: ").append(doc.get("email"));
                    //   fields.append("\nPhone: ").append(doc.get("phone"));
                    username.setText(u);
                    String link_image = chatUsers.getImageUrl();
                    System.out.println(link_image+"----------ImageResume");
                    if(!(link_image.equals("not_selected")))
                    {
                        Picasso.get().load(link_image).into(profilepic);
                        Picasso.get().load(link_image).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                                scaledBitmap = getCircularBitmap(scaledBitmap);
                                Drawable d = new BitmapDrawable(getResources(), scaledBitmap);
                                toolbar_features.setNavigationIcon(d);
                                //getSupportActionBar().setDisplayShowHomeEnabled(true);
                                // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                    else {



                        if (u.startsWith("a") || u.startsWith("A")) {
                            toolbar_features.setNavigationIcon(R.drawable.a_un);
                            profilepic.setImageResource(R.drawable.a_un);

                        }
                        if (u.startsWith("b") || u.startsWith("B")) {
                            toolbar_features.setNavigationIcon(R.drawable.b_letter);
                            profilepic.setImageResource(R.drawable.b_letter);

                        }
                        if (u.startsWith("c") || u.startsWith("C")) {
                            toolbar_features.setNavigationIcon(R.drawable.c);
                            profilepic.setImageResource(R.drawable.c);

                        }
                        if (u.startsWith("d") || u.startsWith("D")) {
                            toolbar_features.setNavigationIcon(R.drawable.d);
                            profilepic.setImageResource(R.drawable.d);
                            profilepic.buildDrawingCache();
                            bitmap = profilepic.getDrawingCache();

                        }
                        if (u.startsWith("e") || u.startsWith("E")) {
                            toolbar_features.setNavigationIcon(R.drawable.e);
                            profilepic.setImageResource(R.drawable.e);

                        }
                        if (u.startsWith("f") || u.startsWith("F")) {
                            toolbar_features.setNavigationIcon(R.drawable.f);
                            profilepic.setImageResource(R.drawable.f);

                        }
                        if (u.startsWith("g") || u.startsWith("G")) {
                            toolbar_features.setNavigationIcon(R.drawable.g);
                            profilepic.setImageResource(R.drawable.g);

                        }
                        if (u.startsWith("h") || u.startsWith("H")) {
                            toolbar_features.setNavigationIcon(R.drawable.h);
                            profilepic.setImageResource(R.drawable.h);

                        }
                        if (u.startsWith("i") || u.startsWith("I")) {
                            toolbar_features.setNavigationIcon(R.drawable.i);
                            profilepic.setImageResource(R.drawable.i);

                        }
                        if (u.startsWith("j") || u.startsWith("J")) {
                            toolbar_features.setNavigationIcon(R.drawable.j);
                            profilepic.setImageResource(R.drawable.j);

                        }
                        if (u.startsWith("k") || u.startsWith("K")) {
                            toolbar_features.setNavigationIcon(R.drawable.k);
                            profilepic.setImageResource(R.drawable.k);

                        }
                        if (u.startsWith("l") || u.startsWith("L")) {
                            toolbar_features.setNavigationIcon(R.drawable.l_un);
                            profilepic.setImageResource(R.drawable.l_un);

                        }
                        if (u.startsWith("m") || u.startsWith("M")) {
                            toolbar_features.setNavigationIcon(R.drawable.m);
                            profilepic.setImageResource(R.drawable.m);

                        }
                        if (u.startsWith("n") || u.startsWith("N")) {
                            toolbar_features.setNavigationIcon(R.drawable.ic_n);
                            profilepic.setImageResource(R.drawable.ic_n);

                        }
                        if (u.startsWith("o") || u.startsWith("O")) {
                            toolbar_features.setNavigationIcon(R.drawable.o);
                            profilepic.setImageResource(R.drawable.o);

                        }
                        if (u.startsWith("p") || u.startsWith("P")) {
                            toolbar_features.setNavigationIcon(R.drawable.p_un);
                            profilepic.setImageResource(R.drawable.p_un);

                        }
                        if (u.startsWith("q") || u.startsWith("Q")) {
                            toolbar_features.setNavigationIcon(R.drawable.q);
                            profilepic.setImageResource(R.drawable.q);

                        }
                        if (u.startsWith("r") || u.startsWith("R")) {
                            toolbar_features.setNavigationIcon(R.drawable.r);
                            profilepic.setImageResource(R.drawable.r);

                        }
                        if (u.startsWith("s") || u.startsWith("S")) {
                            toolbar_features.setNavigationIcon(R.drawable.s);
                            profilepic.setImageResource(R.drawable.s);

                        }
                        if (u.startsWith("t") || u.startsWith("T")) {
                            toolbar_features.setNavigationIcon(R.drawable.t);
                            profilepic.setImageResource(R.drawable.t);

                        }
                        if (u.startsWith("u") || u.startsWith("U")) {
                            toolbar_features.setNavigationIcon(R.drawable.u);
                            profilepic.setImageResource(R.drawable.u);

                        }
                        if (u.startsWith("v") || u.startsWith("V")) {
                            toolbar_features.setNavigationIcon(R.drawable.v);
                            profilepic.setImageResource(R.drawable.v);

                        }
                        if (u.startsWith("w") || u.startsWith("W")) {
                            toolbar_features.setNavigationIcon(R.drawable.w);
                            profilepic.setImageResource(R.drawable.w);

                        }
                        if (u.startsWith("x") || u.startsWith("X")) {
                            toolbar_features.setNavigationIcon(R.drawable.x);
                            profilepic.setImageResource(R.drawable.x);

                        }
                        if (u.startsWith("y") || u.startsWith("Y")) {
                            toolbar_features.setNavigationIcon(R.drawable.y);
                            profilepic.setImageResource(R.drawable.y);

                        }
                        if (u.startsWith("z") || u.startsWith("Z")) {
                            toolbar_features.setNavigationIcon(R.drawable.z);
                            profilepic.setImageResource(R.drawable.z);

                        }
                        if (u.startsWith("0")) {
                            toolbar_features.setNavigationIcon(R.drawable.no0);
                            profilepic.setImageResource(R.drawable.no0);

                        }
                        if (u.startsWith("1")) {
                            toolbar_features.setNavigationIcon(R.drawable.no1);
                            profilepic.setImageResource(R.drawable.no1);

                        }
                        if (u.startsWith("2")) {
                            toolbar_features.setNavigationIcon(R.drawable.no2);
                            profilepic.setImageResource(R.drawable.no2);

                        }
                        if (u.startsWith("3")) {
                            toolbar_features.setNavigationIcon(R.drawable.no3);
                            profilepic.setImageResource(R.drawable.no3);

                        }
                        if (u.startsWith("4")) {
                            toolbar_features.setNavigationIcon(R.drawable.no4);
                            profilepic.setImageResource(R.drawable.no4);

                        }
                        if (u.startsWith("5")) {
                            toolbar_features.setNavigationIcon(R.drawable.no5);
                            profilepic.setImageResource(R.drawable.no5);

                        }
                        if (u.startsWith("6")) {
                            toolbar_features.setNavigationIcon(R.drawable.no6);
                            profilepic.setImageResource(R.drawable.no6);

                        }
                        if (u.startsWith("8")) {
                            toolbar_features.setNavigationIcon(R.drawable.no8);
                            profilepic.setImageResource(R.drawable.no8);

                        }
                        if (u.startsWith("7")) {
                            toolbar_features.setNavigationIcon(R.drawable.no7);
                            profilepic.setImageResource(R.drawable.no7);

                        }
                        if (u.startsWith("9")) {
                            toolbar_features.setNavigationIcon(R.drawable.no9);
                            profilepic.setImageResource(R.drawable.no9);

                        }
                    }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("STOPPED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("DESTROYED");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        //   signout = findViewById(R.id.signout);
        toolbar_features = findViewById(R.id.toolbar_features);
        drawerLayout = findViewById(R.id.dlayout);
        navigationView = findViewById(R.id.navigationView);


//        navController = Navigation.findNavController(this, R.id.main);
        //firebaseDatabase = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID =  firebaseAuth.getCurrentUser().getUid();
        System.out.println(userID);
        DocumentReference documentReference = firestore.collection("users").document(userID);
//
         setSupportActionBar(toolbar_features);
      //  getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#EFF3FB")));
        getSupportActionBar().setElevation(0);


        googleSignInClient = GoogleSignIn.getClient(this,GoogleSignInOptions.DEFAULT_SIGN_IN);
        //setSupportActionBar(toolbar1);
     /*   if(getSupportActionBar()!= null)
        {
            getSupportActionBar().setTitle("Chalkboard");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
*/
    //    toolbar1.setTitleTextColor(Color.parseColor("#2D2A2E"));
        username = navigationView.getHeaderView(0).findViewById(R.id.username);
        profilepic = navigationView.getHeaderView(0).findViewById(R.id.profilepic);


        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener1);
        load(ClassesFragment.newInstance());
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main,new HomeFragment()).commit();




        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar_features, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        toolbar.setNavigationIcon(R.drawable.ic_person_outline_black_24dp);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        activity_name = getClass().getSimpleName();
        System.out.println(""+ activity_name);     //   normal_signin = getIntent().getExtras().getBoolean("Normal_signin");
        //   NavController navController = Navigation.findNavController(this,R.id.main);
         //  AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(new int[]{R.id.nav_classes,R.id.nav_schedule,R.id.nav_home,R.id.nav_message,R.id.nav_notifications}).setDrawerLayout(drawerLayout).build();
//            NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
    //      NavigationUI.setupWithNavController(navigationView,navController);
     //      NavigationUI.setupWithNavController(bottomNavigationView,navController);

   /*     databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usernameref = databaseReference.child("users").child(firebaseAuth.getUid());

        usernameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //   ForSignupDatabase forSignupDatabase = snapshot.getValue(ForSignupDatabase.class);
                    u = dataSnapshot.child("username").getValue().toString();
                    username.setText(u);

                    if (u.startsWith("n") || u.startsWith("N")) {
                        toolbar1.setNavigationIcon(R.drawable.n);
                        profilepic.setImageResource(R.drawable.n);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        do\\
*/
   documentReference.get().addOnCompleteListener(new OnCompleteListener< DocumentSnapshot >() {
       @Override
       public void onComplete(@NonNull Task < DocumentSnapshot > task) {
           if (task.isSuccessful()) {

               DocumentSnapshot doc = task.getResult();
               ChatUser chatUsers = doc.toObject(ChatUser.class);
               System.out.println(chatUsers.getImageUrl());
               StringBuilder fields = new StringBuilder("");
               u = fields.append(doc.get("username")).toString();
               //  fields.append("\nEmail: ").append(doc.get("email"));
               //   fields.append("\nPhone: ").append(doc.get("phone"));
               username.setText(u);
               getSupportActionBar().setTitle(u+"'s Professional Board");
               toolbar_features.setTitleTextColor(Color.BLACK);

               String link_image = chatUsers.getImageUrl();
               if(!(link_image.equals("not_selected")))
               {
                   Picasso.get().load(link_image).into(profilepic);

                  /* final Target mTarget = new Target() {
                       @Override
                       public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                           System.out.println("onbitmaploaded");
                           Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                           scaledBitmap = getCircularBitmap(scaledBitmap);
                           BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), scaledBitmap);
                           toolbar_features.setNavigationIcon(mBitmapDrawable);
                       }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            System.out.println("Failed loading image");
                        }

                       @Override
                       public void onPrepareLoad(Drawable drawable) {
                           System.out.println("prepare loading image");
                       }
                   };*/


                   Picasso.get().load(link_image).into(mTarget);
                   /*Picasso.get().load(link_image).into(new Target() {
                       @Override
                       public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                           Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                           scaledBitmap = getCircularBitmap(scaledBitmap);
                           Drawable d = new BitmapDrawable(getResources(), scaledBitmap);
                           toolbar_features.setNavigationIcon(d);
                          //getSupportActionBar().setDisplayShowHomeEnabled(true);
                          // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                       }

                       @Override
                       public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                           System.out.println("Failed loading image");
                       }

                       @Override
                       public void onPrepareLoad(Drawable placeHolderDrawable) {
                           System.out.println("prepare loading image");

                       }
                   });*/
               }
               else {



                   if (u.startsWith("a") || u.startsWith("A")) {
                       toolbar_features.setNavigationIcon(R.drawable.a_un);
                       profilepic.setImageResource(R.drawable.a_un);

                   }
                   if (u.startsWith("b") || u.startsWith("B")) {
                       toolbar_features.setNavigationIcon(R.drawable.b_letter);
                       profilepic.setImageResource(R.drawable.b_letter);

                   }
                   if (u.startsWith("c") || u.startsWith("C")) {
                       toolbar_features.setNavigationIcon(R.drawable.c);
                       profilepic.setImageResource(R.drawable.c);

                   }
                   if (u.startsWith("d") || u.startsWith("D")) {
                       toolbar_features.setNavigationIcon(R.drawable.d);
                       profilepic.setImageResource(R.drawable.d);
                       profilepic.buildDrawingCache();
                       bitmap = profilepic.getDrawingCache();

                   }
                   if (u.startsWith("e") || u.startsWith("E")) {
                       toolbar_features.setNavigationIcon(R.drawable.e);
                       profilepic.setImageResource(R.drawable.e);

                   }
                   if (u.startsWith("f") || u.startsWith("F")) {
                       toolbar_features.setNavigationIcon(R.drawable.f);
                       profilepic.setImageResource(R.drawable.f);

                   }
                   if (u.startsWith("g") || u.startsWith("G")) {
                       toolbar_features.setNavigationIcon(R.drawable.g);
                       profilepic.setImageResource(R.drawable.g);

                   }
                   if (u.startsWith("h") || u.startsWith("H")) {
                       toolbar_features.setNavigationIcon(R.drawable.h);
                       profilepic.setImageResource(R.drawable.h);

                   }
                   if (u.startsWith("i") || u.startsWith("I")) {
                       toolbar_features.setNavigationIcon(R.drawable.i);
                       profilepic.setImageResource(R.drawable.i);

                   }
                   if (u.startsWith("j") || u.startsWith("J")) {
                       toolbar_features.setNavigationIcon(R.drawable.j);
                       profilepic.setImageResource(R.drawable.j);

                   }
                   if (u.startsWith("k") || u.startsWith("K")) {
                       toolbar_features.setNavigationIcon(R.drawable.k);
                       profilepic.setImageResource(R.drawable.k);

                   }
                   if (u.startsWith("l") || u.startsWith("L")) {
                       toolbar_features.setNavigationIcon(R.drawable.l_un);
                       profilepic.setImageResource(R.drawable.l_un);

                   }
                   if (u.startsWith("m") || u.startsWith("M")) {
                       toolbar_features.setNavigationIcon(R.drawable.m);
                       profilepic.setImageResource(R.drawable.m);

                   }
                   if (u.startsWith("n") || u.startsWith("N")) {
                       toolbar_features.setNavigationIcon(R.drawable.ic_n);
                       profilepic.setImageResource(R.drawable.ic_n);

                   }
                   if (u.startsWith("o") || u.startsWith("O")) {
                       toolbar_features.setNavigationIcon(R.drawable.o);
                       profilepic.setImageResource(R.drawable.o);

                   }
                   if (u.startsWith("p") || u.startsWith("P")) {
                       toolbar_features.setNavigationIcon(R.drawable.p_un);
                       profilepic.setImageResource(R.drawable.p_un);

                   }
                   if (u.startsWith("q") || u.startsWith("Q")) {
                       toolbar_features.setNavigationIcon(R.drawable.q);
                       profilepic.setImageResource(R.drawable.q);

                   }
                   if (u.startsWith("r") || u.startsWith("R")) {
                       toolbar_features.setNavigationIcon(R.drawable.r);
                       profilepic.setImageResource(R.drawable.r);

                   }
                   if (u.startsWith("s") || u.startsWith("S")) {
                       toolbar_features.setNavigationIcon(R.drawable.s);
                       profilepic.setImageResource(R.drawable.s);

                   }
                   if (u.startsWith("t") || u.startsWith("T")) {
                       toolbar_features.setNavigationIcon(R.drawable.t);
                       profilepic.setImageResource(R.drawable.t);

                   }
                   if (u.startsWith("u") || u.startsWith("U")) {
                       toolbar_features.setNavigationIcon(R.drawable.u);
                       profilepic.setImageResource(R.drawable.u);

                   }
                   if (u.startsWith("v") || u.startsWith("V")) {
                       toolbar_features.setNavigationIcon(R.drawable.v);
                       profilepic.setImageResource(R.drawable.v);

                   }
                   if (u.startsWith("w") || u.startsWith("W")) {
                       toolbar_features.setNavigationIcon(R.drawable.w);
                       profilepic.setImageResource(R.drawable.w);

                   }
                   if (u.startsWith("x") || u.startsWith("X")) {
                       toolbar_features.setNavigationIcon(R.drawable.x);
                       profilepic.setImageResource(R.drawable.x);

                   }
                   if (u.startsWith("y") || u.startsWith("Y")) {
                       toolbar_features.setNavigationIcon(R.drawable.y);
                       profilepic.setImageResource(R.drawable.y);

                   }
                   if (u.startsWith("z") || u.startsWith("Z")) {
                       toolbar_features.setNavigationIcon(R.drawable.z);
                       profilepic.setImageResource(R.drawable.z);

                   }
                   if (u.startsWith("0")) {
                       toolbar_features.setNavigationIcon(R.drawable.no0);
                       profilepic.setImageResource(R.drawable.no0);

                   }
                   if (u.startsWith("1")) {
                       toolbar_features.setNavigationIcon(R.drawable.no1);
                       profilepic.setImageResource(R.drawable.no1);

                   }
                   if (u.startsWith("2")) {
                       toolbar_features.setNavigationIcon(R.drawable.no2);
                       profilepic.setImageResource(R.drawable.no2);

                   }
                   if (u.startsWith("3")) {
                       toolbar_features.setNavigationIcon(R.drawable.no3);
                       profilepic.setImageResource(R.drawable.no3);

                   }
                   if (u.startsWith("4")) {
                       toolbar_features.setNavigationIcon(R.drawable.no4);
                       profilepic.setImageResource(R.drawable.no4);

                   }
                   if (u.startsWith("5")) {
                       toolbar_features.setNavigationIcon(R.drawable.no5);
                       profilepic.setImageResource(R.drawable.no5);

                   }
                   if (u.startsWith("6")) {
                       toolbar_features.setNavigationIcon(R.drawable.no6);
                       profilepic.setImageResource(R.drawable.no6);

                   }
                   if (u.startsWith("8")) {
                       toolbar_features.setNavigationIcon(R.drawable.no8);
                       profilepic.setImageResource(R.drawable.no8);

                   }
                   if (u.startsWith("7")) {
                       toolbar_features.setNavigationIcon(R.drawable.no7);
                       profilepic.setImageResource(R.drawable.no7);

                   }
                   if (u.startsWith("9")) {
                       toolbar_features.setNavigationIcon(R.drawable.no9);
                       profilepic.setImageResource(R.drawable.no9);

                   }
               }

           }
       }
   })
           .addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
               }
           });
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UserProfile.class);
                intent.putExtra("username",u);
                startActivity(intent);
            }
        });
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

       actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_person_outline_black_24dp);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                     prof = documentSnapshot.getString("choice");
                     if(prof.equals("Professional teacher / Home tutor")){
                         navigationView.getMenu().findItem(R.id.switch_profile).setEnabled(true);
                     }
                     else{
                         navigationView.getMenu().findItem(R.id.switch_profile).setEnabled(false);
                     }


                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
      /*  gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
         // bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //  getSupportFragmentManager().beginTransaction().replace(R.id.main,new HomeFragment()).commit();
*/
    }

    private Bitmap getCircularBitmap(Bitmap scaledBitmap) {
        // Calculate the circular bitmap width with border
        int squareBitmapWidth = Math.min(scaledBitmap.getWidth(), scaledBitmap.getHeight());
        // Initialize a new instance of Bitmap
        Bitmap dstBitmap = Bitmap.createBitmap (
                squareBitmapWidth, // Width
                squareBitmapWidth, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Canvas canvas = new Canvas(dstBitmap);
        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // Calculate the left and top of copied bitmap
        float left = (squareBitmapWidth-scaledBitmap.getWidth())/2;
        float top = (squareBitmapWidth-scaledBitmap.getHeight())/2;
        canvas.drawBitmap(scaledBitmap, left, top, paint);
        // Free the native object associated with this bitmap.
        scaledBitmap.recycle();
        // Return the circular bitmap
        return dstBitmap;
    }


    /*  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
              Fragment selectedfragment = null;
              switch (menuItem.getItemId()) {
                  case R.id.classesFragment:
                      selectedfragment = new ClassesFragment();
                      load(selectedfragment);
                      return true;

                  case R.id.scheduleFragment:
                      selectedfragment = new ScheduleFragment();
                      load(selectedfragment);
                      return true;

                  case R.id.homeFragment:
                      selectedfragment = new HomeFragment();
                      load(selectedfragment);
                      return true;

                  case R.id.chatsFragment:
                      selectedfragment = new ChatsFragment();
                      load(selectedfragment);
                      return true;

                  case R.id.notificationsFragment:
                      selectedfragment = new NotificationsFragment();
                      load(selectedfragment);
                      return true;
              }
              return false;
          }
      };

  */
    private void load(Fragment selectedfragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_main, selectedfragment);
        // transaction.addToBackStack(null);
        /* Comment this line and it should work!*/
        transaction.addToBackStack(null);
        transaction.commit();

    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener1 =
    new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem selectedMenuItem) {
            switch (selectedMenuItem.getItemId()) {
                case R.id.nav_classes :
                    load(ClassesFragment.newInstance());
                    return true;
                case R.id.nav_schedule :
                    load(new ScheduleFragment());
                    return true;
                case R.id.nav_home :
                    load(new HomeFragment());
                    return true;
                case R.id.nav_message :
                    load(new ChatsFragment());
                    return true;
                case R.id.nav_notifications :
                    load(new NotificationsFragment());
                    return true;

            }

          /*  if (selectedMenuItem != mActiveBottomNavigationViewMenuItem){
                mActiveBottomNavigationViewMenuItem.setChecked(false);
                mActiveBottomNavigationViewMenuItem = selectedMenuItem;
            }
*/
            return false;
        }
    };
//    private void setupBottomNavView()// aita call koi ditaso?
//    {
//        Menu bottomNavigationViewMenu = bottomNavigationView.getMenu();
//        bottomNavigationViewMenu.findItem(R.id.nav_classes).setChecked(false);
//        mActiveBottomNavigationViewMenuItem = bottomNavigationViewMenu.findItem(R.id.nav_home).setChecked(true);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem selectedMenuItem) {
//                switch (selectedMenuItem.getItemId()) {
//                    case R.id.nav_classes :
//                        load(new ClassesFragment());
//                        return true;
//                    case R.id.nav_schedule :
//                        load(new ScheduleFragment());
//                        return true;
//                    case R.id.nav_home :
//                        load(new HomeFragment());
//                        return true;
//                    case R.id.nav_message :
//                        load(new ChatsFragment());
//                        return true;
//                    case R.id.nav_notifications :
//                        load(new NotificationsFragment());
//                        return true;
//
//                }
//
//                if (selectedMenuItem != mActiveBottomNavigationViewMenuItem){
//                    mActiveBottomNavigationViewMenuItem.setChecked(false);
//                    mActiveBottomNavigationViewMenuItem = selectedMenuItem;
//                }
//
//                return false;
//            }
//        });
//
//    }
//
//
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.logout:


             /*   if(normal_signin=true){
                    FirebaseAuth.getInstance().signOut();
                }*/

//                else{
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                FirebaseAuth.getInstance().signOut();
                            }
                        }
                    });
            //    }


                Intent intent = new Intent(Features.this, Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
               /* Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
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
                startActivity(new Intent(getApplicationContext(), Update_profile.class));
                break;
        }
        switch (menuItem.getItemId()) {
            case R.id.switch_profile:
                if(activity_name.equals("Features"))
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity_HomeTutor.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(), Features.class));
                }

                break;
        }
        return true;
    }
}
