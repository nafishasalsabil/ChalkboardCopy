package com.example.chalkboard_copy;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

class ClassPerformanceHTAdapter extends RecyclerView.Adapter<ClassPerformanceHTAdapter.ClassPerformanceHTViewHolder> {

   static Context context;
    List<PerformanceClass> performanceClassList;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
List<StudentItems> itemsList = new ArrayList<>();
   DocumentReference documentReference;
    String lectureName,title,section;
    DocumentReference documentReference1;
    int i=0;
    int listSize=0;
    int g = 0;
    double attendance = 0;
    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSection(String section) {
        this.section = section;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ClassPerformanceHTAdapter(Context context, List<PerformanceClass> performanceClassList) {
        this.performanceClassList = performanceClassList;
        this.context = context;
        setHasStableIds(true);


    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ClassPerformanceHTViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView roll;
        TextView name;
        TextView marks;

        public ClassPerformanceHTViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll_perf);
            name = itemView.findViewById(R.id.name_perf);
            marks = itemView.findViewById(R.id.performance_marks);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.edit_class_layout);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    EditText editcourseno = (EditText) dialog.findViewById(R.id.edit_courseno);

                    EditText editcoursetype= (EditText) dialog.findViewById(R.id.edit_coursetype);

                    EditText editcredithour = (EditText) dialog.findViewById(R.id.edit_credithours);

                    //  System.out.println(classitems.get(position).getCredits());
                    Button update = (Button) dialog.findViewById(R.id.updatebutton);
                    Button cancel = (Button) dialog.findViewById(R.id.cancelbutton);
*//*

                }
            });*/
        }
    }

    @NonNull
    @Override
    public ClassPerformanceHTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.performance_item, parent, false);
        return new ClassPerformanceHTViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassPerformanceHTViewHolder holder, int position) {
      //  holder.setIsRecyclable(false);
        holder.roll.setText(Integer.toString(performanceClassList.get(position).getId()));
        holder.name.setText(performanceClassList.get(position).getName());
        CollectionReference collectionReference1 = firestore.collection("users").document(userID)
                .collection("Courses").document(title)
                .collection("Batches").document(section).collection("Attendance");
        collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                itemsList.clear();
                List<StudentItems> documentData = queryDocumentSnapshots.toObjects(StudentItems.class);
                itemsList.addAll(documentData);
                Log.d("checkListSuiize",itemsList.size()+"");
                g = itemsList.size();

            }
        });
        DocumentReference documentReference2 =  firestore.collection("users").document(userID)
                .collection("Courses").document(title)
                .collection("Batches").document(section)
                .collection("Class_Performance").document(holder.roll.getText().toString())
                .collection("Marks").document("Coverted_to_100");
        documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                PercentageClass percentageClass = documentSnapshot.toObject(PercentageClass.class);
                holder.marks.setText(Double.toString(percentageClass.getPercentage())+"%");
            }
        });


     /*   CollectionReference  collectionReference2 = firestore.collection("users").document(userID)
                .collection("Courses").document(title)
                .collection("Batches").document(section)
                .collection("Class_Performance");
        collectionReference2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<PerformanceClass> documentData1 = queryDocumentSnapshots.toObjects(PerformanceClass.class);

                for(PerformanceClass performanceClass : documentData1)
                {
                    System.out.println(performanceClass.getId());
                    Log.d("checkLI",listSize+"");

                     documentReference1 = collectionReference2.document(Integer.toString(performanceClass.getId()));
                    documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                            Log.d("checkAtt",performanceClass.getCount()+"");

                            if(performanceClass.getCount()==itemsList.size())
                            {

                                attendance = 10;
                                Log.d("checkAtt",attendance+"");
                                DocumentReference documentReference1 =  firestore.collection("users").document(userID)
                                        .collection("Courses").document(title)
                                        .collection("Batches").document(section)
                                        .collection("Class_Performance").document(holder.roll.getText().toString())
                                        .collection("Backup").document("Coverted_to_20");
                                Map<String, Object> user1 = new HashMap<>();
                                user1.put("attendance", attendance);
                                user1.put("grade","not_given");
                                user1.put("cgpa",0);
                                user1.put("final_marks",0);
                                user1.put("thirtybackup", 0);

                                documentReference1.set(user1, SetOptions.merge());
                                DocumentReference collectionReference =  firestore.collection("users").document(userID)
                                        .collection("Courses").document(title)
                                        .collection("Batches").document(section)
                                        .collection("Class_Performance").document(holder.roll.getText().toString())
                                        .collection("Backup").document("Coverted_to_20");
                                collectionReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                                        double s2 = performanceClass.getConverted_quiz();
                                        Log.d("checkS1",attendance+"");
                                        Log.d ("checkS2",s2+"");

                                        double  sum = attendance+s2;
                                        Log.d ("checkSUM",sum+"");

                                        holder.marks.setText(sum+"");
                                        DocumentReference documentReference0 =  firestore.collection("users").document(userID)
                                                .collection("Courses").document(title)
                                                .collection("Batches").document(section)
                                                .collection("Class_Performance").document(holder.roll.getText().toString())
                                                .collection("Backup").document("Coverted_to_20");
                                        Map<String, Object> user0 = new HashMap<>();
                                        user0.put("thirtybackup", sum);
                                        documentReference0.update(user0);



                                    }
                                });


                            }
                            else{
                                if(performanceClass.getCount()<itemsList.size() && performanceClass.getCount()>((itemsList.size()*60)/100))
                                {
                                    attendance =7;
                                    Log.d("checkAtt",attendance+"");
                                    DocumentReference documentReference2 =  firestore.collection("users").document(userID)
                                            .collection("Courses").document(title)
                                            .collection("Sections").document(section)
                                            .collection("Class_Performance").document(holder.roll.getText().toString())
                                            .collection("Backup").document("Coverted_to_20");
                                    Map<String, Object> user1 = new HashMap<>();
                                    user1.put("attendance", attendance);
                                    user1.put("grade","not_given");
                                    user1.put("cgpa",0);
                                    user1.put("final_marks",0);
                                    user1.put("thirtybackup", 0);
                                    documentReference2.set(user1, SetOptions.merge());
                                    DocumentReference collectionReference =  firestore.collection("users").document(userID)
                                            .collection("Courses").document(title)
                                            .collection("Sections").document(section)
                                            .collection("Class_Performance").document(holder.roll.getText().toString())
                                            .collection("Backup").document("Coverted_to_20");
                                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                                            double s2 = performanceClass.getConverted_quiz();
                                            Log.d("checkS1",attendance+"");
                                            Log.d ("checkS2",s2+"");

                                            double  sum = attendance+s2;
                                            Log.d ("checkSUM",sum+"");

                                            holder.marks.setText(sum+"");

                                            DocumentReference documentReference0 =  firestore.collection("users").document(userID)
                                                    .collection("Courses").document(title)
                                                    .collection("Sections").document(section)
                                                    .collection("Class_Performance").document(holder.roll.getText().toString())
                                                    .collection("Backup").document("Coverted_to_20");
                                            Map<String, Object> user0 = new HashMap<>();
                                            user0.put("thirtybackup", sum);
                                            documentReference0.update(user0);


                                        }
                                    });


                                }
                                else if(performanceClass.getCount()<((itemsList.size()*60)/100))

                                {
                                    attendance =4;
                                    Log.d("checkAtt",attendance+"");
                                    DocumentReference documentReference3 =  firestore.collection("users").document(userID)
                                            .collection("Courses").document(title)
                                            .collection("Sections").document(section)
                                            .collection("Class_Performance").document(holder.roll.getText().toString())
                                            .collection("Backup").document("Coverted_to_20");
                                    Map<String, Object> user1 = new HashMap<>();
                                    user1.put("attendance", attendance);
                                    user1.put("grade","not_given");
                                    user1.put("cgpa",0);
                                    user1.put("final_marks",0);
                                    user1.put("thirtybackup", 0);
                                    documentReference3.set(user1, SetOptions.merge());
                                    DocumentReference collectionReference =  firestore.collection("users").document(userID)
                                            .collection("Courses").document(title)
                                            .collection("Sections").document(section)
                                            .collection("Class_Performance").document(holder.roll.getText().toString())
                                            .collection("Backup").document("Coverted_to_20");
                                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                                            double s2 = performanceClass.getConverted_quiz();
                                            Log.d("checkS1",attendance+"");
                                            Log.d ("checkS2",s2+"");

                                            double  sum = attendance+s2;
                                            Log.d ("checkSUM",sum+"");

                                            holder.marks.setText(sum+"");
                                            DocumentReference documentReference0 =  firestore.collection("users").document(userID)
                                                    .collection("Courses").document(title)
                                                    .collection("Sections").document(section)
                                                    .collection("Class_Performance").document(holder.roll.getText().toString())
                                                    .collection("Backup").document("Coverted_to_20");
                                            Map<String, Object> user0 = new HashMap<>();
                                            user0.put("thirtybackup", sum);
                                            documentReference0.update(user0);


                                        }
                                    });


                                }
                            }



                        }
                    });
                }
            }
        });
*/
       /* DocumentReference collectionReference =  firestore.collection("users").document(userID)
                .collection("Courses").document(title)
                .collection("Sections").document(section)
                .collection("Class_Performance").document(holder.roll.getText().toString())
                .collection("Backup").document("Coverted_to_20");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PerformanceClass performanceClass = documentSnapshot.toObject(PerformanceClass.class);
                double s1 =  performanceClass.getAttendance() ;
                double s2 = performanceClass.getConverted_quiz();
                 Log.d("checkS1",s1+"");
                Log.d ("checkS2",s2+"");

                double  sum = s1+s2;
                Log.d ("checkSUM",sum+"");

                holder.marks.setText(sum+"");


            }
        });
       */ /*collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<PerformanceClass> documentData1 = queryDocumentSnapshots.toObjects(PerformanceClass.class);

                    for(PerformanceClass performanceClass : documentData1)
                    {
                       double s1 =  performanceClass.getAttendance() ;
                       double s2 = performanceClass.getConverted_quiz();
                        Log.d("checkS1",s1+"");
                        Log.d ("checkS2",s2+"");

                      double  sum = s1+s2;
                        Log.d ("checkSUM",sum+"");

                        holder.marks.setText(sum+"");
                      //  sum=0;
                    }
            }
        });

*/
   /*     DocumentReference documentReference3 = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                .collection("Attendance").document(Lecture_s).collection("Status").document(Integer.toString(studentItems1.getId()));
*/
      /*  documentReference = firestore.collection("users").document(userID)
                .collection("Courses").document(title).collection("Sections")
                .document(section)
                .collection("Attendance").document(lectureName).collection("Status").document(Integer.toString(studentItemsrecord.get(position).getId()));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                StatusClass statusClass = documentSnapshot.toObject(StatusClass.class);

                holder.status.setText(statusClass.getStatus());
                Log.d("checkS",statusClass.getStatus());
                if(statusClass.getStatus().equals("present"))
                {
                    i++;
                    Log.d("checkStatus",i+"");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });*/
       // System.out.println(studentItemsrecord.get(position).getStatus());


    }


    @Override
    public int getItemCount() {
        return performanceClassList.size();
    }
}
