package com.example.chalkboard_copy;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

class StudentInfoHomeTutorAdapter extends RecyclerView.Adapter<StudentInfoHomeTutorAdapter.studentinfohometutorViewHolder> {

    Context context;
    List<StudentItems> studentItems1 = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    private DocumentReference documentReference;
    CollectionReference collectionReference;
    StudentItems studentItems_object = new StudentItems();
    String ui="";
    String lectureName,title,section;
    List<PerformanceClass> quizitems1= new ArrayList<>();
    List<PerformanceClass> quizitems2= new ArrayList<>();
    List<PercentageClass> quizitems3= new ArrayList<>();
    List<IncomeClass> list3 = new ArrayList<>();


    public void setTitle(String title) {
        this.title = title;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public static final String TAG = "check";
    SharedPreferences sharedPreferences1, sharedPreferences2, sharedPreferences3;


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentInfoHomeTutorAdapter(Context context, List<StudentItems> studentItems1) {
        this.studentItems1 = studentItems1;
//        this.context = context;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

    public static class studentinfohometutorViewHolder extends RecyclerView.ViewHolder {
        TextView roll;
        TextView name;
        CardView cardView;
        ImageView info;

        public studentinfohometutorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll_info_ht);
            name = itemView.findViewById(R.id.name_info_ht);

            info = itemView.findViewById(R.id.info_image_ht);
//            pres = itemView.findViewById(R.id.present_status);
//            abs = itemView.findViewById(R.id.absent_status);
//            la = itemView.findViewById(R.id.late_status);



        }
    }

    @NonNull
    @Override
    public studentinfohometutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_info_item_home_tutor, parent, false);
        context = parent.getContext();
        return new studentinfohometutorViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull studentinfohometutorViewHolder holder, int position) {
        holder.roll.setText(Integer.toString(studentItems1.get(position).getId()));
        holder.name.setText(studentItems1.get(position).getName());

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*StudentInfoDialog studentInfoDialog = new StudentInfoDialog();
                StudentInfo studentInfo = (StudentInfo) context;
                studentInfoDialog.show( studentInfo.getSupportFragmentManager(), "studentinfodialog");
*/
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.student_info_home_tutor);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView t1 = dialog.findViewById(R.id.payment);
                TextView t2 = dialog.findViewById(R.id.percentage);
           //     TextView t3 = dialog.findViewById(R.id.cg_info);
           //     dialog.create();
                dialog.show();
             DocumentReference  studentcollection = firestore.collection("users").document(userID)
                        .collection("Courses").document(title).collection("Batches")
                        .document(section)
                        .collection("Class_Performance")
                     .document(Integer.toString(studentItems1.get(position).getId())).collection("Marks").document("Coverted_to_100");
         studentcollection.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
             @Override
             public void onSuccess(DocumentSnapshot documentSnapshot) {
                PercentageClass percentageClass = documentSnapshot.toObject(PercentageClass.class);
                quizitems3.add(percentageClass);
                 System.out.println(percentageClass.getPercentage());
                 t2.setText(Double.toString(percentageClass.getPercentage()));
             }
         });
         CollectionReference collectionReference =  firestore.collection("users").document(userID)
                 .collection("Courses").document(title).collection("Batches")
                 .document(section)
                 .collection("Monthly_Payments");
         collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 List<IncomeClass> doc3 = queryDocumentSnapshots.toObjects(IncomeClass.class);
                 list3.addAll(doc3);
                 for(int i=0;i<list3.size();i++)
                 {
                     System.out.println(list3.get(i).getPayment());
                     t1.setText(list3.get(i).getPayment());


                 }

             }
         });

            /* studentcollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                 @Override
                 public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<PercentageClass> doc = queryDocumentSnapshots.toObjects(PercentageClass.class);
                    quizitems3.addAll(doc);

                    t1.setText(Double.toString(quizitems3.get(position).getPercentage()));
                     System.out.println(Double.toString(quizitems3.get(position).getPercentage()));

                 }
             });
*/

              /*  CollectionReference collectionReference =  firestore.collection("users").document(userID)
                        .collection("Courses").document(title)
                        .collection("Sections").document(section)
                        .collection("Class_Performance").document(holder.roll.getText().toString())
                        .collection("Backup");
                collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<PerformanceClass> doc = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                        quizitems1.addAll(doc);
                        for(PerformanceClass performanceClass : doc)
                        {
                            t3.setText(Double.toString(performanceClass.getCgpa()));
                           t2.setText(performanceClass.getGrade());

                        }

                    }
                });
                CollectionReference collectionReference2 =  firestore.collection("users").document(userID)
                        .collection("Courses").document(title)
                        .collection("Sections").document(section)
                        .collection("Class_Performance");
                collectionReference2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<PerformanceClass> doc = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                        quizitems2.addAll(doc);
                        for(PerformanceClass performanceClass : doc)
                        {
                            t1.setText(Integer.toString(quizitems2.get(position).getCount()));

                        }

                    }
                });


*/
            }
        });

    }

  /*  private int getcolor(int position) {
        String status = studentItems.get(position).getStatus();
        if(status.equals("P"))
        {
            return Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        }
        else if(status.equals("A"))
        {
            return Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));

        }
        return Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(context,R.color.normal)));


    }*/

    @Override
    public int getItemCount() {
        return studentItems1.size();
    }
}
