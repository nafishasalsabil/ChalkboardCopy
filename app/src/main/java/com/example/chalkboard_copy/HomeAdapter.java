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

class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    Context context;
    List<ScheduleClass> list;


    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public HomeAdapter(Context context, List<ScheduleClass> list) {
        this.list = list;
        this.context = context;


    }


    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView course, section, time, room;


        public HomeViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            course = itemView.findViewById(R.id.course_name_home);
            section = itemView.findViewById(R.id.section_home);
            time = itemView.findViewById(R.id.time_home);
            room = itemView.findViewById(R.id.room_home);
            //itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new HomeViewHolder(itemView, onItemClickListener);


    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.course.setText(list.get(position).getCourse_name());
        holder.section.setText(list.get(position).getSection());
        holder.time.setText(list.get(position).getTime());
        holder.room.setText(list.get(position).getRoom());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


}
