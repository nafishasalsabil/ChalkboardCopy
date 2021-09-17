package com.example.chalkboard_copy;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

//import static com.example.chalkboardnew.Attendance_activity.Lecture_s;
//import static com.example.chalkboardnew.Attendance_activity.id1;


class StudentListHometutorAdapter extends RecyclerView.Adapter<StudentListHometutorAdapter.studentlisthometutorViewHolder> {

    Context context;
    List<StudentItems> studentItems = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    private DocumentReference documentReference;
    CollectionReference collectionReference;
    StudentItems studentItems_object = new StudentItems();
    String ui="";




    public static final String TAG = "check";
    SharedPreferences sharedPreferences1, sharedPreferences2, sharedPreferences3;


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public StudentListHometutorAdapter(Context context, List<StudentItems> studentItems) {
        this.studentItems = studentItems;
        this.context = context;
    }

    public void setUi(String ui) {
        this.ui = ui;
    }

    public static class studentlisthometutorViewHolder extends RecyclerView.ViewHolder {
        TextView roll;
        TextView name;
        CardView cardView;

        public studentlisthometutorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll_list_ht);
            name = itemView.findViewById(R.id.name_list_ht);
            cardView = itemView.findViewById(R.id.cardview);
     }
    }

    @NonNull
    @Override
    public studentlisthometutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item_list_only_hometutor, parent, false);
        return new studentlisthometutorViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull studentlisthometutorViewHolder holder, int position) {
        holder.roll.setText(Integer.toString(studentItems.get(position).getId()));
        holder.name.setText(studentItems.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }
}
