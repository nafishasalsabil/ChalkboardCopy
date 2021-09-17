package com.example.chalkboard_copy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView dayname,datetoday;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    List<ScheduleClass> scheduleClassList = new ArrayList<>();
    List<ScheduleClass> scheduleClassList1 = new ArrayList<>();

    RecyclerView home_recyleview;
    String class_name = "";
    HomeAdapter homeAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
            View v =  inflater.inflate(R.layout.fragment_home,container,false);
            home_recyleview = v.findViewById(R.id.home_recycler_view);
        home_recyleview.setHasFixedSize(true);
       LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        home_recyleview.setLayoutManager(layoutManager);
            dayname = v.findViewById(R.id.day_name);
            datetoday = v.findViewById(R.id.date_today);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        Log.d("checkDate",dayOfTheWeek);
        dayname.setText(dayOfTheWeek);
       /* Date currentTime = Calendar.getInstance().getTime();
        Log.d("checkDate",currentTime.toString());
       */ calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        datetoday.setText(date);
        Log.d("checkDate",date);
        /*SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("day", dayOfTheWeek); // Storing string
        editor.commit();
        String vu = pref.getString("key_name", null);
        assert vu != null;
        if(vu.equals(dayOfTheWeek))
        {
            Log.d("checkDay",dayOfTheWeek);
        }
   */     CollectionReference collectionReference = firestore.collection("users").document(userID)
                .collection("Schedules");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
              //  List<ScheduleClass> doc = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    ScheduleClass scheduleClass = documentSnapshot.toObject(ScheduleClass.class);
//                    System.out.println(scheduleClass.getDay());
                    if(scheduleClass.getDay().equals(dayOfTheWeek))
                    {
                       // List<ScheduleClass> doc = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                        scheduleClassList1.add(scheduleClass);
                        homeAdapter = new HomeAdapter(getContext(),scheduleClassList1);

                        System.out.println("HIII");
                        System.out.println(scheduleClassList1);
                        Log.d("checkDate",scheduleClassList1.toString());

                        home_recyleview.setVisibility(View.VISIBLE);
                        // Log.d("checkL",scheduleClassList1);


                       /* scheduleClassList.add(scheduleClass);
                        System.out.println(scheduleClassList);
                        class_name = scheduleClass.getCourse_name();
                        System.out.println(class_name);
                        Log.d("checkList",scheduleClassList.toString());
                        DocumentReference documentReference = firestore.collection("users").document(userID)
                                .collection("Today")
                                .document(scheduleClass.getCourse_name());
                        documentReference.set(scheduleClass);
*/


                    }
                }
                homeAdapter = new HomeAdapter(getContext(),scheduleClassList1);

                home_recyleview.setAdapter(homeAdapter);
                homeAdapter.notifyDataSetChanged();
              /*  CollectionReference collectionReference1 =  firestore.collection("users").document(userID)
                                .collection("Today");
                        collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<ScheduleClass> doc = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                                homeAdapter = new HomeAdapter(getContext(),scheduleClassList1);
                                home_recyleview.setAdapter(homeAdapter);
                                scheduleClassList1.addAll(doc);
                                System.out.println("HIII");
                                System.out.println(scheduleClassList1);
                                // Log.d("checkL",scheduleClassList1);
                                homeAdapter.notifyDataSetChanged();

                    }
                });*/
            }
        });

       /* DocumentReference documentReference =  firestore.collection("users").document(userID)
                .collection("Today").document(dayOfTheWeek)
            ;
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ScheduleClass scheduleClass = documentSnapshot.toObject(ScheduleClass.class);
                homeAdapter = new HomeAdapter(getContext(),scheduleClassList1);
                home_recyleview.setAdapter(homeAdapter);
                scheduleClassList1.add(scheduleClass);
                System.out.println(scheduleClassList1);
                Log.d("checkL",scheduleClassList1.toString());
                homeAdapter.notifyDataSetChanged();

            }
        });*/
        /*.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<ScheduleClass> doc = new ArrayList<>();
                homeAdapter = new HomeAdapter(getContext(),scheduleClassList1);
                home_recyleview.setAdapter(homeAdapter);
                scheduleClassList1.addAll(doc);
                homeAdapter.notifyDataSetChanged();



            }
        });*/



        return v;
    }
}
