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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class All_Students_All_Attendance_Record_Home_TutorAdapter extends RecyclerView.Adapter<All_Students_All_Attendance_Record_Home_TutorAdapter.All_Students_All_Attendance_Record_Home_TutorViewHolder> {

    Context context;
    List<StudentItems> studentItemsrecord;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    DocumentReference documentReference;
    String lectureName,title,section;
    int i=0;

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

    public All_Students_All_Attendance_Record_Home_TutorAdapter(Context context, List<StudentItems> studentItemsrecord) {
        this.studentItemsrecord = studentItemsrecord;
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

    public static class All_Students_All_Attendance_Record_Home_TutorViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView roll;
        TextView name;
        TextView status;

        public All_Students_All_Attendance_Record_Home_TutorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            roll = itemView.findViewById(R.id.roll_record_ht);
            name = itemView.findViewById(R.id.name_record_ht);
            status = itemView.findViewById(R.id.status_text_ht);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    onItemClickListener.onClick(ClassViewHolder.this.getAdapterPosition());
                /*    Intent intent = new Intent(v.getContext(),Sections_Inside_Courses.class);
                    intent.putExtra("Title",classname.getText());
                    // System.out.println(classname.getText());
                    v.getContext().startActivity(intent);
*/                }
            });
        }
    }

    @NonNull
    @Override
    public All_Students_All_Attendance_Record_Home_TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_students_all_attendance_record_item_home_tutor, parent, false);
        return new All_Students_All_Attendance_Record_Home_TutorViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull All_Students_All_Attendance_Record_Home_TutorViewHolder holder, int position) {
     //   holder.setIsRecyclable(false);
        holder.roll.setText(Integer.toString(studentItemsrecord.get(position).getId()));
        holder.name.setText(studentItemsrecord.get(position).getName());
   /*     DocumentReference documentReference3 = firestore.collection("users").document(userID)
                .collection("Courses").document(clicked_courseTitle).collection("Sections").document(clicked_course_section)
                .collection("Attendance").document(Lecture_s).collection("Status").document(Integer.toString(studentItems1.getId()));
*/
        documentReference = firestore.collection("users").document(userID)
                .collection("Courses").document(title).collection("Batches")
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
        });
        System.out.println(studentItemsrecord.get(position).getStatus());

    }


    @Override
    public int getItemCount() {
        return studentItemsrecord.size();
    }
}
