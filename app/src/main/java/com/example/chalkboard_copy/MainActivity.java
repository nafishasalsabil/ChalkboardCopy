package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button getstartedbutton;
    Animation topanim,bottomanim,leftanim;
    ImageView img;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseUser != null) {
           /* Intent intent = new Intent(getApplicationContext(), MainActivity_HomeTutor.class);

            startActivity(intent);
*/
            String userID = firebaseAuth.getCurrentUser().getUid();

            DocumentReference documentReference = firestore.collection("users").document(userID);
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                   ForSignupDatabase doc = documentSnapshot.toObject(ForSignupDatabase.class);
                  //  System.out.println(doc.getChoice());
                 //   Log.d("checkChoice",doc.getChoice());
                    if(doc.getChoice()!=null)
                    {
                        if(doc.getChoice().equals("Professional teacher"))
                        {
                            Intent intent = new Intent(getApplicationContext(), Features.class);

                            startActivity(intent);
                        }
                        else if(doc.getChoice().equals("Home tutor"))
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity_HomeTutor.class);

                            startActivity(intent);

                        }
                        else if(doc.getChoice().equals("Professional teacher / Home tutor"))
                        {
                            Intent intent = new Intent(getApplicationContext(), Features.class);

                            startActivity(intent);

                            Toast.makeText(MainActivity.this,"You are logged in as a professional teacher! You can switch profiles from here!",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);

                            startActivity(intent);



                        }
                    }


                }
            });
        }
        /*else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

            startActivity(intent);
            finish();
        }*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        /*topanim= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        bottomanim= AnimationUtils.loadAnimation(this,R.anim.bottom_anim);
        leftanim= AnimationUtils.loadAnimation(this,R.anim.left_anim);*/
       img = findViewById(R.id.img);
        //img.setAnimation(topanim);
        getstartedbutton = findViewById(R.id.getstarted);
        //getstartedbutton.setAnimation(bottomanim);
        getstartedbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        openActivity2();
    }

    private void openActivity2() {
        Intent intent = new Intent(this,Main2Activity.class);
        startActivity(intent);
    }
}
