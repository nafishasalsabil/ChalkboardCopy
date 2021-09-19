package com.example.chalkboard_copy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassActivity extends AppCompatActivity {

    private Button submit;
    private EditText email;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        submit = (Button)findViewById(R.id.forgotpassbutton);
        email = (EditText)findViewById(R.id.email);
        firebaseAuth = FirebaseAuth.getInstance();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ForgotPassActivity.this,"Please check your email",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ForgotPassActivity.this,Main2Activity.class));
                    }
                    else
                    {
                        Toast.makeText(ForgotPassActivity.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    }
                });
            }
        });
    }
}
