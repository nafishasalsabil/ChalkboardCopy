package com.example.chalkboard_copy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>{
    private List<ReminderClass> allReminders = new ArrayList<>();
    private TextView message,time;
    Context context;
    public ReminderAdapter(Context context, List<ReminderClass> allReminders) {
        this.context = context;
        this.allReminders = allReminders;
    }


    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item,parent,false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapter.ReminderViewHolder holder, int position) {
        ReminderClass reminders = allReminders.get(position);
        if(!reminders.getMessage().equals(""))
            message.setText(reminders.getMessage());
        else
            message.setHint("No Message");

        String pattern ="EEE, d MMM yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(reminders.getRemindDate());
        System.out.println(date);
        time.setText(date);
    }

    @Override
    public int getItemCount() {
        return allReminders.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.description_reminder);
            time = itemView.findViewById(R.id.time_reminder);
        }
    }
}
