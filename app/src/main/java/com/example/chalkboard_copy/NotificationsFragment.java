package com.example.chalkboard_copy;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;

public class NotificationsFragment extends Fragment {
    private FloatingActionButton add;
    private Dialog dialog;
  //  private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private List<ReminderClass> temp = new ArrayList<>();
    private TextView empty;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();

    private int mYear, mMonth, mDay, mHour, mMinute;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_notifications,container,false);
        add =view.findViewById(R.id.add_reminder);
      //  empty = findViewById(R.id.empty);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReminder();
            }
        });
        recyclerView = view.findViewById(R.id.reminder_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        CollectionReference collectionReference = firestore.collection("users").document(userID)
                .collection("Reminders");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ReminderClass> doc = queryDocumentSnapshots.toObjects(ReminderClass.class);
                adapter = new ReminderAdapter(getContext(),temp);
                recyclerView.setAdapter(adapter);
                temp.addAll(doc);
                adapter.notifyDataSetChanged();

            }
        });

      //  setItemsInRecyclerView();

        return view;
    }

    private void addReminder() {
        AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.add_reminder_dialog, null);
        alerDialog2.setView(view);
        AlertDialog dialog = alerDialog2.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

         TextView date = dialog.findViewById(R.id.reminder_date);
        TextView time = dialog.findViewById(R.id.reminder_time);
        EditText ddescription = dialog.findViewById(R.id.reminder_description);

        Button cancel,add_b;
        cancel = dialog.findViewById(R.id.cancel_reminder_button);
        add_b = dialog.findViewById(R.id.add_reminder_button);


        final Calendar newCalender = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth) {

                        final Calendar newDate = Calendar.getInstance();
                        Calendar newTime = Calendar.getInstance();
                        String date_date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        date.setText(date_date);


                    }
                },newCalender.get(Calendar.YEAR),newCalender.get(Calendar.MONTH),newCalender.get(Calendar.DAY_OF_MONTH));

                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();

            }
        });
        final Calendar newDate = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                Log.d("checktime",hourOfDay + ":" + minute);
                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
    /*    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedTimeFormat(hourOfDay);
                select_time.setText(hourOfDay+":"+minute + format);
            }
        },hour,minute,true);
        timePickerDialog.show();
    }*/

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                ReminderClass reminders = new ReminderClass();
                //  assert message != null;
                 String m = ddescription.getText().toString().trim();
                if (TextUtils.isEmpty(ddescription.getText().toString().trim())) {
                    ddescription.setError("Empty!");
                } else {

                    try {


                        reminders.setMessage(ddescription.getText().toString().trim());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date selectedDate = sdf.parse(date.getText().toString().trim()+" "+time.getText().toString());
                        long millis = selectedDate.getTime();
                        Date remind = new Date(millis);
                        Log.d("checkedtime", date.getText().toString().trim());

                        String d = date.getText().toString().trim();


                        reminders.setRemindDate(remind);
                        // roomDAO.Insert(reminders);
                        //    List<ReminderClass> l = roomDAO.getAll();
                        //   reminders = l.get(l.size()-1);
                        Log.d("ID chahiye", reminders.getId() + "");

                        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+6:00"));
                        calendar.setTime(remind);
                        calendar.set(Calendar.SECOND, 0);
//com.support.v4. Sunday,Sept 20,2020,1:30pm
                        Log.d("checktime", reminders.getRemindDate().toString());
                        Intent intent = new Intent(getContext(), NotifierAlarm.class);
                        intent.putExtra("Message", reminders.getMessage());
                        intent.putExtra("RemindDate", reminders.getRemindDate().toString());
                        intent.putExtra("id", reminders.getId());
                        PendingIntent intent1 = PendingIntent.getBroadcast(getContext(), reminders.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);

                        Toast.makeText(getContext(), "Inserted Successfully", Toast.LENGTH_SHORT).show();
                        setItemsInRecyclerView();
                        DocumentReference documentReference = firestore.collection("users").document(userID)
                                .collection("Reminders").document(m);
                        ReminderClass reminderClass = new ReminderClass(reminders.getId(), m, remind);
                        documentReference.set(reminderClass);
                        //   AppDatabase.destroyInstance();
                        dialog.dismiss();
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }

                }
            }
        });


    }
    public void setItemsInRecyclerView(){

     /*   RoomDAO dao = appDatabase.getRoomDAO();
        temp = dao.orderThetable();
      */ // if(temp.size()>0) {
         //   empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
     //   }   studentListAdapter = new StudentListAdapter(getApplicationContext(), studentItems);
        //                recyclerView.setAdapter(studentListAdapter);
        //
        adapter = new ReminderAdapter(getContext(),temp);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
