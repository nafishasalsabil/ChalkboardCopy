
package com.example.chalkboard_copy;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProfileHomeTutor extends AppCompatActivity {

    TextView un,profession,email,no_of_courses;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    List<ChatUser> list = new ArrayList<>();
    StorageReference storageReference;
    ImageButton profile_pic_upload;
    public static final int  IMAGE_REQUEST = 1;
    private Uri image_url;
    private StorageTask uploadTask;
    ImageView pro_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_hometutor);
        un=  findViewById(R.id.username_textview_ht);
        profession = findViewById(R.id.prof_textview_ht);
        email = findViewById(R.id.email_text_ht);
        no_of_courses = findViewById(R.id.no_of_courses_ht);
        Toolbar toolbar_user  = findViewById(R.id.toolbar_profile_ht);
        setSupportActionBar(toolbar_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_user.setNavigationIcon(R.drawable.ic_back);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_user.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pro_pic = findViewById(R.id.pic_profile_ht);
        profile_pic_upload = findViewById(R.id.profile_pic_upload_ht);
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        un.setText(username);
        DocumentReference documentReference = firestore.collection("users").document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               ChatUser chatUsers = documentSnapshot.toObject(ChatUser.class);
                System.out.println(chatUsers.getEmail());
                profession.setText(chatUsers.getChoice());
                email.setText(chatUsers.getEmail());

            }
        });
        CollectionReference collectionReference = firestore.collection("users").document(userID).collection("Courses");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<CourseInfo> courseInfoList = queryDocumentSnapshots.toObjects(CourseInfo.class);

                System.out.println(courseInfoList.size());
                no_of_courses.setText(String.valueOf(courseInfoList.size()));
            }
        });

        profile_pic_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference = firestore.collection("users").document(userID);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ChatUser chatUsers = documentSnapshot.toObject(ChatUser.class);
                System.out.println(chatUsers.getImageUrl());
                String link_image = chatUsers.getImageUrl();
                if(link_image.equals("not_selected"))
                {
                    pro_pic.setBackgroundResource(R.drawable.ic_profile);
                }
                else
                {
                    Picasso.get().load(link_image).into(pro_pic);

                }

            }
        });
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadImage()
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading image");
        progressDialog.show();

        if(image_url!=null)
        {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." +getFileExtension(image_url));
            uploadTask = reference.putFile(image_url);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                   if(!task.isSuccessful())
                   {
                       throw task.getException();
                   }
                   return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        DocumentReference documentReference = firestore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("imageUrl",mUri);
                        documentReference.set(user, SetOptions.merge());
                        CollectionReference collectionReference = firestore.collection("Notices");
                        Map<String,Object> user1 = new HashMap<>();
                        user.put("imageUrl",mUri);
                        collectionReference.add(user1);

                        progressDialog.dismiss();
                        DocumentReference documentReference1 = firestore.collection("users").document(userID);

                        documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ChatUser chatUsers = documentSnapshot.toObject(ChatUser.class);
                                System.out.println(chatUsers.getImageUrl());
                                String link_image = chatUsers.getImageUrl();
                                if(link_image.equals("not_selected"))
                                {
                                    pro_pic.setBackgroundResource(R.drawable.ic_profile);
                                }
                                else
                                {
                                    Picasso.get().load(link_image).into(pro_pic);

                                }

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No image selected",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            image_url = data.getData();
            if(uploadTask!=null && uploadTask.isInProgress())
            {
                Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else {
                uploadImage();
            }
        }
    }
}