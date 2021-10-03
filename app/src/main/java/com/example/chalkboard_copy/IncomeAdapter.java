package com.example.chalkboard_copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeViewHolder> {

    Context context;
    List<IncomeClass> quizitems;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;
    public static String sec = "",title = "";
    List<IncomeClass> classList = new ArrayList<>();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public IncomeAdapter(Context context, List<IncomeClass> quizitems) {
        this.quizitems = quizitems;
        this.context = context;


    }


    public static class IncomeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView roll,name;
        Button checkBox;

        public IncomeViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll_income_ht);
            name = itemView.findViewById(R.id.name_income_ht);
            checkBox = itemView.findViewById(R.id.button_paid);


        }
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_student_item_ht, parent, false);
        return new IncomeViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        holder.name.setText(quizitems.get(position).getName());
        holder.roll.setText(Integer.toString(quizitems.get(position).getId()));

        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){
                    CollectionReference collectionReference =  firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Courses").document(title)
                            .collection("Batches").document(sec)
                            .collection("Monthly_Payments");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<IncomeClass> incomeClassList = queryDocumentSnapshots.toObjects(IncomeClass.class);
                            classList.addAll(incomeClassList);

                            for(IncomeClass incomeClass : classList)
                            {
                                System.out.println(incomeClass.getPayment());
                                holder.checkBox.setText(classList.get(position).getPayment());
                            }
                        }
                    });
                    holder.checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(holder.checkBox.getText().toString().equals("unpaid"))
                            {
                                holder.checkBox.setText("paid");
                                DocumentReference documentReference5 =  firestore.collection("users")
                                        .document(userID).collection("All Files")
                                        .document(HT).collection("Courses").document(title)
                                        .collection("Batches").document(sec)
                                        .collection("Monthly_Payments").document(Integer.toString(quizitems.get(position).getId()));
                                Map<String, Object> user1 = new HashMap<>();
                                user1.put("payment","paid");
                                documentReference5.update(user1);
                            }
                            else if(holder.checkBox.getText().toString().equals("paid"))
                            {
                                holder.checkBox.setText("unpaid");
                                DocumentReference documentReference5 =  firestore.collection("users")
                                        .document(userID).collection("All Files")
                                        .document(HT).collection("Courses").document(title)
                                        .collection("Batches").document(sec)
                                        .collection("Monthly_Payments").document(Integer.toString(quizitems.get(position).getId()));
                                Map<String, Object> user1 = new HashMap<>();
                                user1.put("payment","unpaid");
                                documentReference5.update(user1);
                            }
                        }
                    });



                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    CollectionReference collectionReference = firestore.collection("users").document(userID)
                            .collection("Courses").document(title)
                            .collection("Batches").document(sec)
                            .collection("Monthly_Payments");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<IncomeClass> incomeClassList = queryDocumentSnapshots.toObjects(IncomeClass.class);
                            classList.addAll(incomeClassList);

                            for(IncomeClass incomeClass : classList)
                            {
                                System.out.println(incomeClass.getPayment());
                                holder.checkBox.setText(classList.get(position).getPayment());
                            }
                        }
                    });
                    holder.checkBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(holder.checkBox.getText().toString().equals("unpaid"))
                            {
                                holder.checkBox.setText("paid");
                                DocumentReference documentReference5 = firestore.collection("users").document(userID)
                                        .collection("Courses").document(title)
                                        .collection("Batches").document(sec)
                                        .collection("Monthly_Payments").document(Integer.toString(quizitems.get(position).getId()));
                                Map<String, Object> user1 = new HashMap<>();
                                user1.put("payment","paid");
                                documentReference5.update(user1);
                            }
                            else if(holder.checkBox.getText().toString().equals("paid"))
                            {
                                holder.checkBox.setText("unpaid");
                                DocumentReference documentReference5 = firestore.collection("users").document(userID)
                                        .collection("Courses").document(title)
                                        .collection("Batches").document(sec)
                                        .collection("Monthly_Payments").document(Integer.toString(quizitems.get(position).getId()));
                                Map<String, Object> user1 = new HashMap<>();
                                user1.put("payment","unpaid");
                                documentReference5.update(user1);
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
        return quizitems.size();
    }
}
