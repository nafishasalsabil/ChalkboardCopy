package com.example.chalkboard_copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

class HomeHomeTutorAdapter extends RecyclerView.Adapter<HomeHomeTutorAdapter.HomeHomeTutorViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    Context context;
    List<ScheduleHomeTutorClass> list;


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeHomeTutorAdapter(Context context, List<ScheduleHomeTutorClass> list) {
        this.list = list;
        this.context = context;


    }


    public static class HomeHomeTutorViewHolder extends RecyclerView.ViewHolder {
        TextView course, section, time, room;


        public HomeHomeTutorViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            course = itemView.findViewById(R.id.course_name_home_ht);
            section = itemView.findViewById(R.id.batch_home);
            time = itemView.findViewById(R.id.time_home_ht);
            //itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public HomeHomeTutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_ht, parent, false);
        return new HomeHomeTutorViewHolder(itemView, onItemClickListener);


    }

    @Override
    public void onBindViewHolder(@NonNull HomeHomeTutorViewHolder holder, int position) {
        holder.course.setText(list.get(position).getCourse_name());
        holder.section.setText(list.get(position).getBatch());
        holder.time.setText(list.get(position).getTime());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}
