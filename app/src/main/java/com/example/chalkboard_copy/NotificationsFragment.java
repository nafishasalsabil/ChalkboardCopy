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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    private int mYear, mMonth, mDay, mHour, mMinute;
    public static int hr=0,min=0;
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

        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                temp.clear();
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){
                    CollectionReference collectionReference = firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(PROF)
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


                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
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


                }

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });





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
                                hr=hourOfDay;
                                min = minute;
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 ReminderClass reminders = new ReminderClass();
                 String m = ddescription.getText().toString().trim();
                if (TextUtils.isEmpty(ddescription.getText().toString().trim())) {
                    ddescription.setError("Empty!");
                }
                else if (TextUtils.isEmpty(date.getText().toString().trim())) {
                    date.setError("Empty!");
                }
                else if (TextUtils.isEmpty(time.getText().toString().trim())) {
                    time.setError("Empty!");
                }
                else {

                    try {
                        reminders.setMessage(ddescription.getText().toString().trim());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date selectedDate = sdf.parse(date.getText().toString().trim()+" "+time.getText().toString());
                        long millis = selectedDate.getTime();
                        Date remind = new Date(millis);
                        Log.d("checkedtime", date.getText().toString().trim());
                        String d = date.getText().toString().trim();
                        reminders.setRemindDate(remind);
                        Intent intent = new Intent(getContext(), NotifierAlarm.class);
                        intent.putExtra("Message", reminders.getMessage());
                        intent.putExtra("RemindDate", reminders.getRemindDate().toString());
                        intent.putExtra("id", reminders.getId());
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                        );
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.HOUR_OF_DAY, hr);
                        startTime.set(Calendar.MINUTE, min);
                        startTime.set(Calendar.SECOND, 0);
                        long alarmStartTime = startTime.getTimeInMillis();
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);

                        Toast.makeText(getContext(), "Inserted Successfully", Toast.LENGTH_SHORT).show();


                        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

                        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot doc = task.getResult();
                                StringBuilder fields = new StringBuilder("");
                                status = fields.append(doc.get("choice")).toString();

                                if(status.equals("Professional teacher / Home tutor")){

                                    DocumentReference documentReference =firestore.collection("users")
                                            .document(userID).collection("All Files")
                                            .document(PROF)
                                            .collection("Reminders").document(m);
                                    ReminderClass reminderClass = new ReminderClass(reminders.getId(), m, remind);
                                    documentReference.set(reminderClass);
                                    setItemsInRecyclerView();

                                }
                                else if(!(status.equals("Professional teacher / Home tutor"))){
                                    DocumentReference documentReference = firestore.collection("users").document(userID)
                                            .collection("Reminders").document(m);
                                    ReminderClass reminderClass = new ReminderClass(reminders.getId(), m, remind);
                                    documentReference.set(reminderClass);
                                    setItemsInRecyclerView();

                                }
                            }
                        }) .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });



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

        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                temp.clear();
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){
                    CollectionReference collectionReference = firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(PROF)
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


                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
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


                }

            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }
}
