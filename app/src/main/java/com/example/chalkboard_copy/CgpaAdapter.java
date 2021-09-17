package com.example.chalkboard_copy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

class CgpaAdapter extends RecyclerView.Adapter<CgpaAdapter.CgpaViewHolder> {

    Context context;
    List<PerformanceClass> quizitems;
    List<PerformanceClass> quizitems1= new ArrayList<>();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    double m=0;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;
    public static String sec = "",title = "";
    String grade_final="";
    double cg =0;
    DocumentReference documentReference2;
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

    public CgpaAdapter(Context context, List<PerformanceClass> quizitems) {
        this.quizitems = quizitems;
        this.context = context;


    }


    public static class CgpaViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView roll;
        TextView name;
        TextView marks_cg;
        TextView grade;

        public CgpaViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll_cg);
            name = itemView.findViewById(R.id.name_cg);
            marks_cg = itemView.findViewById(R.id.cgpa_textView);
            grade = itemView.findViewById(R.id.grade_textView);
            cardView = itemView.findViewById(R.id.cardview_cgpa);


           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.cgpa_dialog_box);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView thirty_backup = dialog.findViewById(R.id.thirty_backup);
                    thirty_backup.setText(Double.toString(quizitems.get(position).getThirtybackup()));
                    EditText final_marks= (EditText) dialog.findViewById(R.id.final_marks);
                    Button calculate = (Button) dialog.findViewById(R.id.cgpa_calculate_btn);

                    calculate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println(final_marks.getText());
                            if(TextUtils.isEmpty(final_marks.getText()))
                            {
                                final_marks.setError("Empty!");
                            }
                        }

                    });
                    dialog.dismiss();
                }
            });*/


        }
    }

    @NonNull
    @Override
    public CgpaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cgpa_item, parent, false);
        context = parent.getContext();
        return new CgpaViewHolder(itemView, onItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull CgpaViewHolder holder, int position) {

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
                    CollectionReference collectionReference= firestore.collection("users")
                            .document(userID).collection("All Files").document(PROF)
                            .collection("Courses").document(title).collection("Sections").document(sec)
                            .collection("Class_Performance")
                            .document(Integer.toString(quizitems.get(position).getId()))
                            .collection("Backup");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<PerformanceClass> doc = queryDocumentSnapshots.toObjects(PerformanceClass.class);

                            for(PerformanceClass performanceClass : doc)
                            {
                                holder.marks_cg.setText(Double.toString(performanceClass.getCgpa()));
                                holder.grade.setText(performanceClass.getGrade());

                            }
                        }
                    });

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("fffffffffffffffffffffffffffffff");
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.cgpa_dialog_box);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            TextView thirty_backup = dialog.findViewById(R.id.thirty_backup);
                            DocumentReference collectionReference = firestore.collection("users")
                                    .document(userID).collection("All Files").document(PROF)
                                    .collection("Courses").document(title).collection("Sections").document(sec)
                                    .collection("Class_Performance")
                                    .document(Integer.toString(quizitems.get(position).getId()))
                                    .collection("Backup").document("Coverted_to_20");

                            collectionReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    DocumentSnapshot doc = task.getResult();
                                    if (task.isSuccessful()) {
                                        List<PerformanceClass> list = new ArrayList<>();
                                      /*  for (DocumentSnapshot document : doc) {
                                            PerformanceClass taskItem = document.toObject(PerformanceClass.class);
                                            list.add(taskItem);
                                        }*/

                                        System.out.println(doc.getDouble("thirtybackup"));
                                        m=doc.getDouble("thirtybackup");
                                        thirty_backup.setText(String.valueOf(doc.getDouble("thirtybackup")));
                                        //Log.d(TAG, list.toString());
                                    }
                                }
                            });
                /*            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<PerformanceClass> doc = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                                    quizitems1.addAll(doc);
                                    System.out.println(quizitems1.size());
                                    m =quizitems.get(position).getThirtybackup();
                                    System.out.println(m);
                                    thirty_backup.setText(Double.toString(quizitems.get(position).getThirtybackup()));

                                }
                            });*/
                            documentReference2= firestore.collection("users")
                                    .document(userID).collection("All Files").document(PROF)
                                    .collection("Courses").document(title).collection("Sections").document(sec)
                                    .collection("Class_Performance")
                                    .document(Integer.toString(quizitems.get(position).getId()))
                                    .collection("Backup").document("Final Marks");

                            EditText final_marks= (EditText) dialog.findViewById(R.id.final_marks);
                            Button calculate = (Button) dialog.findViewById(R.id.cgpa_calculate_btn);

                            calculate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println(final_marks.getText());
                                    if(TextUtils.isEmpty(final_marks.getText()))
                                    {
                                        final_marks.setError("Empty!");

                                    }
                                    else
                                    {
                                        double total = Double.parseDouble(final_marks.getText().toString() )+ m;
                                        System.out.println(total+" Is total");
                                        if(total>=80)
                                        {
                                            grade_final = "A+";
                                            cg = 4.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);
                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=75 && total<80)
                                        {
                                            grade_final = "A";
                                            cg = 3.75;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);
                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=70 && total<75)
                                        {
                                            grade_final = "A-";
                                            cg = 3.5;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=65 && total<70)
                                        {
                                            grade_final = "B+";
                                            cg = 3.25;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=60 && total<65)
                                        {
                                            grade_final = "B";
                                            cg = 3.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=55 && total<60)
                                        {
                                            grade_final = "B-";
                                            cg = 2.75;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=50 && total<55)
                                        {
                                            grade_final = "C+";
                                            cg = 2.5;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=45 && total<50)
                                        {
                                            grade_final = "C";
                                            cg = 2.25;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);
                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>=40 && total<45)
                                        {
                                            grade_final = "D";
                                            cg = 2.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total<40)
                                        {
                                            grade_final = "F";
                                            cg = 0.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        dialog.dismiss();
                /*           holder.marks_cg.setText(Double.toString(cg));
                           holder.grade.setText(grade_final);
                */       }
                                }

                            });
                            dialog.show();

                        }
                    });


                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    CollectionReference collectionReference= firestore.collection("users").document(userID)
                            .collection("Courses").document(title)
                            .collection("Sections").document(sec)
                            .collection("Class_Performance")
                            .document(Integer.toString(quizitems.get(position).getId()))
                            .collection("Backup");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<PerformanceClass> doc = queryDocumentSnapshots.toObjects(PerformanceClass.class);

                            for(PerformanceClass performanceClass : doc)
                            {
                                holder.marks_cg.setText(Double.toString(performanceClass.getCgpa()));
                                holder.grade.setText(performanceClass.getGrade());

                            }
                        }
                    });

                    holder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("fffffffffffffffffffffffffffffff");
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.cgpa_dialog_box);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            TextView thirty_backup = dialog.findViewById(R.id.thirty_backup);
                            CollectionReference collectionReference = firestore.collection("users").document(userID)
                                    .collection("Courses").document(title)
                                    .collection("Sections").document(sec)
                                    .collection("Class_Performance")
                                    .document(Integer.toString(quizitems.get(position).getId()))
                                    .collection("Backup");
                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<PerformanceClass> doc = queryDocumentSnapshots.toObjects(PerformanceClass.class);
                                    quizitems1.addAll(doc);
                                    m =quizitems1.get(position).getThirtybackup();
                                    thirty_backup.setText(Double.toString(quizitems1.get(position).getThirtybackup()));

                                }
                            });
                            documentReference2= firestore.collection("users").document(userID)
                                    .collection("Courses").document(title)
                                    .collection("Sections").document(sec)
                                    .collection("Class_Performance")
                                    .document(Integer.toString(quizitems.get(position).getId()))
                                    .collection("Backup").document("Final Marks");

                            EditText final_marks= (EditText) dialog.findViewById(R.id.final_marks);
                            Button calculate = (Button) dialog.findViewById(R.id.cgpa_calculate_btn);

                            calculate.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println(final_marks.getText());
                                    if(TextUtils.isEmpty(final_marks.getText()))
                                    {
                                        final_marks.setError("Empty!");

                                    }
                                    else
                                    {
                                        double total = Double.parseDouble(final_marks.getText().toString() )+ m;
                                        System.out.println(total);
                                        if(total>80)
                                        {
                                            grade_final = "A+";
                                            cg = 4.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);
                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>75 && total<80)
                                        {
                                            grade_final = "A";
                                            cg = 3.75;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);
                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>70 && total<75)
                                        {
                                            grade_final = "A-";
                                            cg = 3.5;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>65 && total<70)
                                        {
                                            grade_final = "B+";
                                            cg = 3.25;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>60 && total<65)
                                        {
                                            grade_final = "B";
                                            cg = 3.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>55 && total<60)
                                        {
                                            grade_final = "B-";
                                            cg = 2.75;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>50 && total<55)
                                        {
                                            grade_final = "C+";
                                            cg = 2.5;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>45 && total<50)
                                        {
                                            grade_final = "C";
                                            cg = 2.25;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total>40 && total<45)
                                        {
                                            grade_final = "D";
                                            cg = 2.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        else if(total<40)
                                        {
                                            grade_final = "F";
                                            cg = 0.00;
                                            holder.marks_cg.setText(Double.toString(cg));
                                            holder.grade.setText(grade_final);
                                            Map<String, Object> inuser1 = new HashMap<>();
                                            inuser1.put("final_marks", total);

                                            inuser1.put("cgpa", cg);
                                            inuser1.put("grade", grade_final);
                                            documentReference2.set(inuser1, SetOptions.merge());
                                        }
                                        dialog.dismiss();
                /*           holder.marks_cg.setText(Double.toString(cg));
                           holder.grade.setText(grade_final);
                */       }
                                }

                            });
                            dialog.show();

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
