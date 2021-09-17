package com.example.chalkboard_copy;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AddPostActivity extends AppCompatActivity {

    ActionBar actionBar;

    //views

    EditText titleEt,descriptionEt;
    Button uploadBtn;

    //progress bar
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Post");

        //enable back button in actionbar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        pd = new ProgressDialog(this);
        //firebase

        //init views
        titleEt = findViewById(R.id.PostTittleET);
        descriptionEt = findViewById(R.id.PostDescriptionET);
       // uploadBtn = findViewById(R.id.PostUploadBtn);

        //Upload Button click Listener

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //get data(title,description) from edit text
                String title = titleEt.getText().toString().trim();
                String description = descriptionEt.getText().toString().trim();
                if(TextUtils.isEmpty(title))
                {
                    Toast.makeText(AddPostActivity.this,"Enter Title",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(description))
                {
                    Toast.makeText(AddPostActivity.this,"Enter Description",Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    private void uploadData(String title, String description){
        pd.setMessage("Publishing post");
        pd.show();

        //for post-image name, post-id,post publish time

        String timestamp = String.valueOf(System.currentTimeMillis());

        //String filePathName = "Posts/" + "post_" + timestamp;

        //Data Base
    }

    /*
        Check user Status()


     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();    //go to previous activity
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hometutor, menu);

        //menu.findItem(R.id.action_add_post).setVisible(false)

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        /*

        Missing code for database timestamp 7:08 (17)
        Check user status
         */
        return super.onOptionsItemSelected(item);
    }
}