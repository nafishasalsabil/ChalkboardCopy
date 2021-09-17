package com.example.chalkboard_copy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

class ScheduleHomeTutorAdapter extends BaseAdapter {

    Context context;
    private List<ScheduleHomeTutorClass> scheduleClasses = new ArrayList<>();
    private DocumentReference documentReference;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();


    public static final String TAG = "check";

    public ScheduleHomeTutorAdapter(Context context, List<ScheduleHomeTutorClass> scheduleClasses) {
        this.scheduleClasses = scheduleClasses;
        this.context = context;
    }




    class studentViewHolder extends RecyclerView.ViewHolder {


        public studentViewHolder(@NonNull View itemView) {
            super(itemView);


            int pos = getAdapterPosition();


        }


    }

    @Override
    public int getCount() {
        return scheduleClasses.size();
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

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item_home_tutor, null);
      TextView  course = convertView.findViewById(R.id.course_schedule_ht);
       TextView section = convertView.findViewById(R.id.batch_schedule);
        TextView time = convertView.findViewById(R.id.time_class_ht);

        course.setText(scheduleClasses.get(position).getCourse_name());
        section.setText(scheduleClasses.get(position).getBatch());
        time.setText(scheduleClasses.get(position).getTime());



        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


}
