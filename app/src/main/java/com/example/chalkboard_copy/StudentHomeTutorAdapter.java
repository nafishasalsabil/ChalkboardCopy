package com.example.chalkboard_copy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.chalkboard_copy.StudentListHometutor.Lecture_s_ht;

class StudentHomeTutorAdapter extends BaseAdapter {

    Context context;
    private List<StudentItems> studentItems = new ArrayList<>();
    String status = "not_taken";
    private static int p, a, l;
    boolean x = false, y = false, z = false;
    public static final String PRESENT_TEXT = "present";
    public static final String ABS_TEXT = "abs";
    public static final String LATE_TEXT = "late";
    int i = 0;
    private DocumentReference documentReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();

    SparseBooleanArray statusOfPresent = new SparseBooleanArray();

    SparseBooleanArray statusOfAbsent = new SparseBooleanArray();
    SparseBooleanArray statusOfLate = new SparseBooleanArray();
    public static String status_profession = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    public static final String TAG = "check";
    String title,sec,lec;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public void setLec(String lec) {
        this.lec = lec;
    }

    public StudentHomeTutorAdapter(Context context, List<StudentItems> studentItems) {
        this.studentItems = studentItems;
        this.context = context;
        ResetStatus();
    }

    private void ResetStatus() {
        for (StudentItems student : studentItems) {
            student.setStatus("not_taken");
        }
    }


    class studentViewHolder extends RecyclerView.ViewHolder {


        public studentViewHolder(@NonNull View itemView) {
            super(itemView);


            int pos = getAdapterPosition();


        }


    }

    public void setOptions(StudentItems studentItem, int position, RadioGroup radioGroupStatus) {
        radioGroupStatus.setTag(position);

        if (studentItem.attendance) {
            radioGroupStatus.check(studentItem.getCheckedId());

        } else {
            radioGroupStatus.check(-1);
        }

        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int pos = (int) group.getTag();
                StudentItems studentItem = studentItems.get(pos);
                studentItem.attendance = true;
                studentItem.checkedId = checkedId;

            }
        });

    }

    @Override
    public int getCount() {
        return studentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item_home_tutor, null);
        TextView roll = convertView.findViewById(R.id.roll_ht);
        TextView name = convertView.findViewById(R.id.name_ht);
        CardView cardView = convertView.findViewById(R.id.cardview);
        RadioGroup radioGroupStatus = convertView.findViewById(R.id.radioGroup_status_ht);
        RadioButton radioButtonPresent, radioButtonAbs, radioButtonLate;
        radioButtonPresent = convertView.findViewById(R.id.radioButton_present_ht);

        radioButtonAbs = convertView.findViewById(R.id.radioButton_abs_ht);
        radioButtonLate = convertView.findViewById(R.id.radioButton_late_ht);

        roll.setText(Integer.toString(studentItems.get(position).getId()));
        name.setText(studentItems.get(position).getName());
        if (statusOfLate.indexOfKey(position) >= 0) {
            Log.d("checked", "late radio button is setted");
            ((RadioButton) radioGroupStatus.getChildAt(2)).setChecked(true);
//            radioButtonLate.setChecked(true);
//            radioButtonLate.setButtonDrawable(R.drawable.l_filled);


        } else if (statusOfAbsent.indexOfKey(position) >= 0) {
            Log.d("checked", "abs radio button is setted");
            ((RadioButton) radioGroupStatus.getChildAt(1)).setChecked(true);

//            radioButtonAbs.setChecked(true);
//        radioButtonAbs.setButtonDrawable(R.drawable.a_filled);
        } else if (statusOfPresent.indexOfKey(position) >= 0) {
            Log.d("checked", "present radio button is setted");
            ((RadioButton) radioGroupStatus.getChildAt(0)).setChecked(true);

//            radioButtonPresent.setChecked(true);
//            radioButtonPresent.setButtonDrawable(R.drawable.p_filled);
        }

//        holder.setIsRecyclable(false);


        setOptions(studentItems.get(position), position, radioGroupStatus);

        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedItem = radioGroupStatus.getCheckedRadioButtonId();
                if (checkedItem == R.id.radioButton_present_ht) {
                    status = PRESENT_TEXT;

                    if (Objects.equals(studentItems.get(position).getStatus(), null)) {
                        Attendance_activity_hometutor.statusSingleP(true);
                    } else if (Objects.equals(studentItems.get(position).getStatus(), ABS_TEXT)) {
                        Attendance_activity_hometutor.statusP(true, false);
                        statusOfAbsent.delete(statusOfAbsent.indexOfKey(position));

                    } else if (Objects.equals(studentItems.get(position).getStatus(), LATE_TEXT)) {
                        Attendance_activity_hometutor.statusP(false, true);
                        statusOfLate.delete(statusOfLate.indexOfKey(position));
                    }
                    studentItems.get(position).setStatus(status);
                    statusOfPresent.put(position, true);
                    radioButtonPresent.setChecked(true);

                    DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

                    documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            StringBuilder fields = new StringBuilder("");
                            status_profession = fields.append(doc.get("choice")).toString();

                            if(status_profession.equals("Professional teacher / Home tutor")){
                                documentReference = firestore.collection("users")
                                        .document(userID).collection("All Files")
                                        .document(HT).collection("Courses").document(title).collection("Batches").document(sec)
                                        .collection("Attendance").document(Lecture_s_ht).collection("Status").document(Integer.toString(studentItems.get(position).getId()));
                                Map<String, Object> inuser = new HashMap<>();
                                inuser.put("id", studentItems.get(position).getId());
                                inuser.put("name", studentItems.get(position).getName());
                                inuser.put("status", status);

                                documentReference.set(inuser);

                            }
                            else if(!(status_profession.equals("Professional teacher / Home tutor"))){
                                documentReference = firestore.collection("users").document(userID)
                                        .collection("Courses").document(title).collection("Batches").document(sec)
                                        .collection("Attendance").document(Lecture_s_ht).collection("Status").document(Integer.toString(studentItems.get(position).getId()));
                                Map<String, Object> inuser = new HashMap<>();
                                inuser.put("id", studentItems.get(position).getId());
                                inuser.put("name", studentItems.get(position).getName());
                                inuser.put("status", status);

                                documentReference.set(inuser);


                            }





                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });





                } else if (checkedItem == R.id.radioButton_abs_ht) {
                    status = ABS_TEXT;

                    if (Objects.equals(studentItems.get(position).getStatus(), null)) {
                        Attendance_activity_hometutor.statusSingleA(true);
                    } else if (Objects.equals(studentItems.get(position).getStatus(), PRESENT_TEXT)) {
                        Attendance_activity_hometutor.statusA(true, false);
                        statusOfPresent.delete(statusOfPresent.indexOfKey(position));
                    } else if (Objects.equals(studentItems.get(position).getStatus(), LATE_TEXT)) {
                        Attendance_activity_hometutor.statusA(false, true);
                        statusOfLate.delete(statusOfLate.indexOfKey(position));
                    }
                    studentItems.get(position).setStatus(status);
                    statusOfAbsent.put(position, true);
                    radioButtonAbs.setChecked(true);

                    DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

                    documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            StringBuilder fields = new StringBuilder("");
                            status_profession = fields.append(doc.get("choice")).toString();

                            if(status_profession.equals("Professional teacher / Home tutor")){
                                documentReference = firestore.collection("users")
                                        .document(userID).collection("All Files")
                                        .document(HT).collection("Courses").document(title).collection("Batches").document(sec)
                                        .collection("Attendance").document(Lecture_s_ht).collection("Status").document(Integer.toString(studentItems.get(position).getId()));
                                Map<String, Object> inuser = new HashMap<>();
                                inuser.put("id", studentItems.get(position).getId());
                                inuser.put("name", studentItems.get(position).getName());
                                inuser.put("status", status);

                                documentReference.set(inuser);

                            }
                            else if(!(status_profession.equals("Professional teacher / Home tutor"))){
                                documentReference = firestore.collection("users").document(userID)
                                        .collection("Courses").document(title).collection("Batches").document(sec)
                                        .collection("Attendance").document(Lecture_s_ht).collection("Status").document(Integer.toString(studentItems.get(position).getId()));
                                Map<String, Object> inuser = new HashMap<>();
                                inuser.put("id", studentItems.get(position).getId());
                                inuser.put("name", studentItems.get(position).getName());
                                inuser.put("status", status);

                                documentReference.set(inuser);

                            }




                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });



                } else if (checkedItem == R.id.radioButton_late_ht) {
                    status = LATE_TEXT;

                    if (Objects.equals(studentItems.get(position).getStatus(), null)) {
                        Attendance_activity_hometutor.statusSingleL(true);
                    } else if (Objects.equals(studentItems.get(position).getStatus(), PRESENT_TEXT)) {
                        Attendance_activity_hometutor.statusL(true, false);
                        statusOfPresent.delete(statusOfPresent.indexOfKey(position));
                    } else if (Objects.equals(studentItems.get(position).getStatus(), ABS_TEXT)) {
                        Attendance_activity_hometutor.statusL(false, true);
                        statusOfAbsent.delete(statusOfAbsent.indexOfKey(position));
                    }
                    studentItems.get(position).setStatus(status);
                    statusOfLate.put(position, true);
                    radioButtonLate.setChecked(true);

                    DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

                    documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            StringBuilder fields = new StringBuilder("");
                            status_profession = fields.append(doc.get("choice")).toString();

                            if(status_profession.equals("Professional teacher / Home tutor")){
                                documentReference = firestore.collection("users")
                                        .document(userID).collection("All Files")
                                        .document(HT).collection("Courses").document(title).collection("Batches").document(sec)
                                        .collection("Attendance").document(Lecture_s_ht).collection("Status").document(Integer.toString(studentItems.get(position).getId()));
                                Map<String, Object> inuser = new HashMap<>();
                                inuser.put("id", studentItems.get(position).getId());
                                inuser.put("name", studentItems.get(position).getName());
                                inuser.put("status", status);


                                documentReference.update(inuser);

                            }
                            else if(!(status_profession.equals("Professional teacher / Home tutor"))){
                                documentReference = firestore.collection("users").document(userID)
                                        .collection("Courses").document(title).collection("Batches").document(sec)
                                        .collection("Attendance").document(Lecture_s_ht).collection("Status").document(Integer.toString(studentItems.get(position).getId()));
                                Map<String, Object> inuser = new HashMap<>();
                                inuser.put("id", studentItems.get(position).getId());
                                inuser.put("name", studentItems.get(position).getName());
                                inuser.put("status", status);


                                documentReference.update(inuser);

                            }


                        }
                    }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });


                }
//                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
