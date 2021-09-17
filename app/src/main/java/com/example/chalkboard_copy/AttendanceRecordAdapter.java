package com.example.chalkboard_copy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class AttendanceRecordAdapter extends RecyclerView.Adapter<AttendanceRecordAdapter.AttendanceRecordViewHolder> {

    Context context;
    List<StudentItems> studentItems;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;
    String title,section;

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

    public AttendanceRecordAdapter(Context context, List<StudentItems> studentItems) {
        this.studentItems = studentItems;
        this.context = context;


    }


    public static class AttendanceRecordViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView lecture_name;
        TextView lecture_date;
        ImageView edit;


        public AttendanceRecordViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            lecture_name = itemView.findViewById(R.id.lecture_name);
            lecture_date = itemView.findViewById(R.id.lecture_date);
            cardView = itemView.findViewById(R.id.attendancerecordcard);

          /*  itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onClick(ClassViewHolder.this.getAdapterPosition());
                    Intent intent = new Intent(v.getContext(),All_Students_All_Attendance_Record.class);
                    intent.putExtra("lecture",);
                    v.getContext().startActivity(intent);



                }
            });*/
        }
    }

    @NonNull
    @Override
    public AttendanceRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_record_item, parent, false);
        return new AttendanceRecordViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceRecordViewHolder holder, int position) {
        holder.lecture_name.setText(studentItems.get(position).getLecture_name());
        holder.lecture_date.setText(studentItems.get(position).getLecture_date());

        // System.out.println(position);
        //  System.out.println(holder.getAdapterPosition());

        // setPos(holder.getAdapterPosition());

        //    holder.editcoursetitle.setText(classitems.get(position).getCourseTitle());
        // holder.editcourseno.setText(classitems.get(position).getCourseNo());

holder.cardView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(),All_Students_All_Attendance_Record.class);
        intent.putExtra("lecture",studentItems.get(position).getLecture_name());
        intent.putExtra("title",title);
        intent.putExtra("section",section);
        v.getContext().startActivity(intent);

    }
});

    }


    @Override
    public int getItemCount() {
        return studentItems.size();
    }
}
