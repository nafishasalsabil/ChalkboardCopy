package com.example.chalkboard_copy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
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
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

public class ScheduleFragment_HomeTutor extends Fragment {
    CompactCalendarView compactCalendarView;
    TextView textView;
    FloatingActionButton floatingActionButton;
    String format = "";
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    String clicked_date = "";
    //   private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
    SimpleDateFormat currentDay = new SimpleDateFormat("EEEE");
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private boolean shouldShow = false;
    CollectionReference collectionReference;
    ScheduleHomeTutorAdapter adapter;
    String day = "";
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    private int notificationId = 1;
    public static int min=0;
    public static int hr=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_schedule_hometutor,container, false);
        compactCalendarView = view.findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        textView = view.findViewById(R.id.month_title_ht);
        floatingActionButton = view.findViewById(R.id.add_schedule_fab_ht);
        List<ScheduleHomeTutorClass> mutableBookings = new ArrayList<>();

        final ListView bookingsListView = view.findViewById(R.id.schedule_listView_ht);
        adapter = new ScheduleHomeTutorAdapter(getContext(),mutableBookings);
        //  final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.schedule_item,R.id.course_schedule, mutableBookings);
        bookingsListView.setAdapter(adapter);
        compactCalendarView.setUseThreeLetterAbbreviation(false);
      /*  loadEvents();
        loadEventsForYear(2017);
      */  compactCalendarView.invalidate();

//        logEventsByMonth(compactCalendarView);
        clicked_date = dateFormatForDisplaying.format(new Date());
        day = currentDay.format(new Date());
        Log.d("checked time",clicked_date);
        Log.d("checked day",day);
        System.out.println(clicked_date);




        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){

                    collectionReference = firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT).collection("Schedules");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<ScheduleHomeTutorClass> scheduleClassList = queryDocumentSnapshots.toObjects(ScheduleHomeTutorClass.class);
                            //   ScheduleClass scheduleClass = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                            Log.d("checkList",scheduleClassList.size()+"");
                            for(ScheduleHomeTutorClass scheduleClass : scheduleClassList )
                            {
                                System.out.println(scheduleClass.toString());

                                if(scheduleClass.toString().equals(day))
                                {
                                    adapter = new ScheduleHomeTutorAdapter(getContext(),mutableBookings);
                                    mutableBookings.add(scheduleClass);
                                    //  final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.schedule_item,R.id.course_schedule, mutableBookings);
                                    bookingsListView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                }
                            }



                        }
                    });


       /* Event ev1 = new Event(Color.RED, 1477040400000L, "Teachers' Professional Day");
        compactCalendarView.addEvent(ev1);
       */ compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                        @Override
                        public void onDayClick(Date dateClicked) {
                            mutableBookings.clear();
                            adapter.notifyDataSetChanged();
                            textView.setText(dateFormatForMonth.format(dateClicked));
                            List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                            Log.d("check_event", "inside onclick " + dateFormatForDisplaying.format(dateClicked));
                            clicked_date = dateFormatForDisplaying.format(dateClicked);
                            System.out.println(clicked_date);
                            day = currentDay.format(dateClicked);
                            Log.d("checked day",day);

                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<ScheduleHomeTutorClass> scheduleClassList = queryDocumentSnapshots.toObjects(ScheduleHomeTutorClass.class);
                                    //   ScheduleClass scheduleClass = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                                    Log.d("checked tostring",scheduleClassList.toString());
                                    for(ScheduleHomeTutorClass scheduleClass : scheduleClassList )
                                    {
                                        System.out.println(scheduleClass.toString());

                                        if(scheduleClass.toString().equals(day))
                                        {
                                            adapter = new ScheduleHomeTutorAdapter(getContext(),mutableBookings);
                                            mutableBookings.add(scheduleClass);
                                            //  final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.schedule_item,R.id.course_schedule, mutableBookings);
                                            bookingsListView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();

                                        }
                                    }


                                }
                            });


                            //    adapter.notifyDataSetChanged();


               /* if (bookingsFromMap != null) {
                    Log.d("check_event", bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add((String) booking.getData());
                    }
                    adapter.notifyDataSetChanged();
                }*/
                        }

                        @Override
                        public void onMonthScroll(Date firstDayOfNewMonth) {
                            textView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                        }
                    });

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(getContext());
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_dialog_box_hometutor, null);
                            alerDialog2.setView(view);
                            AlertDialog dialog = alerDialog2.create();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            dialog.show();
                            Calendar calendar = Calendar.getInstance();
                            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            final int minute = calendar.get(Calendar.MINUTE);
                            selectedTimeFormat(hour);

                            TextView select_time = view.findViewById(R.id.class_time_ht);
                            EditText course = view.findViewById(R.id.course_name_schedule_dialog_ht);
                            EditText section = view.findViewById(R.id.batch_schedule_dialog_ht);
                            select_time.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            selectedTimeFormat(hourOfDay);
                                            select_time.setText(hourOfDay+":"+minute + format);
                                            hr=hourOfDay;
                                            min = minute;
                                        }
                                    },hour,minute,true);
                                    timePickerDialog.show();
                                }
                            });
                            Button add = (Button) view.findViewById(R.id.add_schedule_class_ht);
                            Button cancel = (Button) view.findViewById(R.id.cancel_schedule_ht);

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                }
                            });
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // showDateAndLectureDialog();
                                    String course_name = course.getText().toString();
                                    String section_name = section.getText().toString();
                                    String time = select_time.getText().toString();

                                    if (course_name.isEmpty() || section_name.isEmpty() || time.isEmpty()) {
                                        course.setError("Please fill up!");
                                        section.setError("Please fill up!");
                                        select_time.setError("Please fill up!");
                                    } else {
                                        String msg = "Course: " + course_name + "," + " Section " + section_name ;
                                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
                                        intent.putExtra("notificationId", notificationId);
                                        intent.putExtra("message", msg);
                                        System.out.println(course_name);

                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                                        );

                                        // AlarmManager
                                        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                                        // Create time.
                                        Calendar startTime = Calendar.getInstance();
                                        startTime.set(Calendar.HOUR_OF_DAY, hr);
                                        startTime.set(Calendar.MINUTE, min);
                                        startTime.set(Calendar.SECOND, 0);
                                        long alarmStartTime = startTime.getTimeInMillis();

                                        // Set Alarm
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);
                                    DocumentReference documentReference = firestore.collection("users")
                                            .document(userID).collection("All Files")
                                            .document(HT).collection("Schedules").document(time);
                                    ScheduleHomeTutorClass scheduleClass = new ScheduleHomeTutorClass(time, course_name, section_name, clicked_date, day);
                                    mutableBookings.add(scheduleClass);

                                    adapter.notifyDataSetChanged();
                                    documentReference.set(scheduleClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getContext(), "The schedule is updated!", Toast.LENGTH_SHORT).show();

                                            //     bookingsListView.setAdapter(adapter);

                                        }
                                    });
                                        dialog.dismiss();

                                    }



                                }
                            });


                        }
                    });


                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    collectionReference = firestore.collection("users")
                            .document(userID).collection("Schedules");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<ScheduleHomeTutorClass> scheduleClassList = queryDocumentSnapshots.toObjects(ScheduleHomeTutorClass.class);
                            //   ScheduleClass scheduleClass = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                            Log.d("checkList",scheduleClassList.size()+"");
                            for(ScheduleHomeTutorClass scheduleClass : scheduleClassList )
                            {
                                System.out.println(scheduleClass.toString());

                                if(scheduleClass.toString().equals(day))
                                {
                                    adapter = new ScheduleHomeTutorAdapter(getContext(),mutableBookings);
                                    mutableBookings.add(scheduleClass);
                                    //  final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.schedule_item,R.id.course_schedule, mutableBookings);
                                    bookingsListView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();

                                }
                            }



                        }
                    });


       /* Event ev1 = new Event(Color.RED, 1477040400000L, "Teachers' Professional Day");
        compactCalendarView.addEvent(ev1);
       */ compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
                        @Override
                        public void onDayClick(Date dateClicked) {
                            mutableBookings.clear();
                            adapter.notifyDataSetChanged();
                            textView.setText(dateFormatForMonth.format(dateClicked));
                            List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                            Log.d("check_event", "inside onclick " + dateFormatForDisplaying.format(dateClicked));
                            clicked_date = dateFormatForDisplaying.format(dateClicked);
                            System.out.println(clicked_date);
                            day = currentDay.format(dateClicked);
                            Log.d("checked day",day);

                            collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<ScheduleHomeTutorClass> scheduleClassList = queryDocumentSnapshots.toObjects(ScheduleHomeTutorClass.class);
                                    //   ScheduleClass scheduleClass = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                                    Log.d("checked tostring",scheduleClassList.toString());
                                    for(ScheduleHomeTutorClass scheduleClass : scheduleClassList )
                                    {
                                        System.out.println(scheduleClass.toString());

                                        if(scheduleClass.toString().equals(day))
                                        {
                                            adapter = new ScheduleHomeTutorAdapter(getContext(),mutableBookings);
                                            mutableBookings.add(scheduleClass);
                                            //  final ArrayAdapter adapter = new ArrayAdapter<>(getContext(), R.layout.schedule_item,R.id.course_schedule, mutableBookings);
                                            bookingsListView.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();

                                        }
                                    }


                                }
                            });


                            //    adapter.notifyDataSetChanged();


               /* if (bookingsFromMap != null) {
                    Log.d("check_event", bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add((String) booking.getData());
                    }
                    adapter.notifyDataSetChanged();
                }*/
                        }

                        @Override
                        public void onMonthScroll(Date firstDayOfNewMonth) {
                            textView.setText(dateFormatForMonth.format(firstDayOfNewMonth));
                        }
                    });

                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alerDialog2 = new AlertDialog.Builder(getContext());
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.schedule_dialog_box_hometutor, null);
                            alerDialog2.setView(view);
                            AlertDialog dialog = alerDialog2.create();
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            dialog.show();
                            Calendar calendar = Calendar.getInstance();
                            final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            final int minute = calendar.get(Calendar.MINUTE);
                            selectedTimeFormat(hour);

                            TextView select_time = view.findViewById(R.id.class_time_ht);
                            EditText course = view.findViewById(R.id.course_name_schedule_dialog_ht);
                            EditText section = view.findViewById(R.id.batch_schedule_dialog_ht);
                            select_time.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                            selectedTimeFormat(hourOfDay);
                                            select_time.setText(hourOfDay+":"+minute + format);
                                            hr=hourOfDay;
                                            min = minute;
                                        }
                                    },hour,minute,true);
                                    timePickerDialog.show();
                                }
                            });
                            Button add = (Button) view.findViewById(R.id.add_schedule_class_ht);
                            Button cancel = (Button) view.findViewById(R.id.cancel_schedule_ht);

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialog.dismiss();

                                }
                            });
                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // showDateAndLectureDialog();
                                    String course_name = course.getText().toString();
                                    String section_name = section.getText().toString();
                                    String time  = select_time.getText().toString();

                                    if (course_name.isEmpty() || section_name.isEmpty() || time.isEmpty()) {
                                        course.setError("Please fill up!");
                                        section.setError("Please fill up!");
                                        select_time.setError("Please fill up!");
                                    } else {
                                        String msg = "Course: " + course_name + "," + " Section " + section_name;
                                        Intent intent = new Intent(getContext(), AlarmReceiver.class);
                                        intent.putExtra("notificationId", notificationId);
                                        intent.putExtra("message", msg);
                                        System.out.println(course_name);

                                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                                getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
                                        );

                                        // AlarmManager
                                        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                                        // Create time.
                                        Calendar startTime = Calendar.getInstance();
                                        startTime.set(Calendar.HOUR_OF_DAY, hr);
                                        startTime.set(Calendar.MINUTE, min);
                                        startTime.set(Calendar.SECOND, 0);
                                        long alarmStartTime = startTime.getTimeInMillis();

                                        // Set Alarm
                                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent);

                                        DocumentReference documentReference = firestore.collection("users")
                                                .document(userID).collection("Schedules").document(time);
                                        ScheduleHomeTutorClass scheduleClass = new ScheduleHomeTutorClass(time, course_name, section_name, clicked_date, day);
                                        mutableBookings.add(scheduleClass);

                                        adapter.notifyDataSetChanged();
                                        documentReference.set(scheduleClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "The schedule is updated!", Toast.LENGTH_SHORT).show();

                                                //     bookingsListView.setAdapter(adapter);

                                            }
                                        });
                                        dialog.dismiss();
                                    }




                                }
                            });


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
    private void selectedTimeFormat(int hour) {
        if(hour==0)
        {
            hour += 12;
            format="am";
        }
        else if(hour==12)
        {
            format="pm";
        }
        else if(hour>12)
        {
            hour -= 12;
            format="pm";
        }
        else
        {
            format = "am";
        }


    }

    @Override
    public void onResume(){
        super.onResume();
        //OnResume Fragment
        textView.setText(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

    }
}
