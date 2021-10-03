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

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

class ClassPerformanceHTAdapter extends RecyclerView.Adapter<ClassPerformanceHTAdapter.ClassPerformanceHTViewHolder> {

   static Context context;
    List<PerformanceClass> performanceClassList;
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
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

    public static Double percentage = 0.0;
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


        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){


                    CollectionReference collectionReference1 =  firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(title)
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
                    DocumentReference documentReference2 =   firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(title)
                            .collection("Batches").document(section)
                            .collection("Class_Performance").document(holder.roll.getText().toString())
                            .collection("Marks").document("Coverted_to_100");
                    documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists())
                            {
//                    PercentageClass percentageClass = documentSnapshot.toObject(PercentageClass.class);
//                    holder.marks.setText(Double.toString(percentageClass.getPercentage())+"%");
                                percentage=documentSnapshot.getDouble("percentage");
                                holder.marks.setText(Double.toString(percentage)+"%");
                            }
                            else{
                                holder.marks.setText("No value");
                            }

                        }
                    });

                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
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

                            if(documentSnapshot.exists())
                            {
//                    PercentageClass percentageClass = documentSnapshot.toObject(PercentageClass.class);
//                    holder.marks.setText(Double.toString(percentageClass.getPercentage())+"%");
                                percentage=documentSnapshot.getDouble("percentage");
                                holder.marks.setText(Double.toString(percentage)+"%");
                            }
                            else{
                                holder.marks.setText("No value");
                            }

                        }
                    });

                }

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });







    }


    @Override
    public int getItemCount() {
        return performanceClassList.size();
    }
}
