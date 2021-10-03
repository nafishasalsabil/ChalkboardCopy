package com.example.chalkboard_copy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment_HomeTutor extends Fragment {
    private TextView dayname,datetoday;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();
    List<ScheduleClass> scheduleClassList = new ArrayList<>();
    List<ScheduleHomeTutorClass> scheduleClassList1 = new ArrayList<>();
    public static String status = "";
    public final String PROF="Professional Account";
    public final String HT="Tutor Account";
    RecyclerView home_recyleview;
    String class_name = "";
    HomeHomeTutorAdapter homeAdapter;
    TextView expected,recieved;
    List<CourseInfoHomeTutorClass> list = new ArrayList<>();
    List<BatchClass> list2 = new ArrayList<>();
    List<IncomeClass> list3 = new ArrayList<>();
    int sum = 0,money=0;
    int payment_counter = 0;
    int paid_sum = 0;
    int remaining_sum = 0;
    int remaining_student = 0;
    TextView remaining_money,total,paid,unpaid_student;
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
    String clicked_date = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_hometutor,container, false);
        home_recyleview = view.findViewById(R.id.home_recycler_view_ht);
        home_recyleview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        home_recyleview.setLayoutManager(layoutManager);
        dayname = view.findViewById(R.id.day_name_ht);
        datetoday = view.findViewById(R.id.date_today_ht);
        expected = view.findViewById(R.id.expected_income_ht);
        recieved = view.findViewById(R.id.recieved_money);
        remaining_money =view.findViewById(R.id.remaining_money);
        total = view.findViewById(R.id.total_students);
        paid = view.findViewById(R.id.paid_stu);
        unpaid_student = view.findViewById(R.id.rem_stu);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        Log.d("checkDate",dayOfTheWeek);
        dayname.setText(dayOfTheWeek);
        clicked_date = dateFormatForDisplaying.format(new Date());
        System.out.println(clicked_date);
       /* Date currentTime = Calendar.getInstance().getTime();
        Log.d("checkDate",currentTime.toString());
       */ calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        datetoday.setText(date);
        Log.d("checkDate",date);


        DocumentReference documentReference_for_status = firestore.collection("users").document(userID);

        documentReference_for_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                StringBuilder fields = new StringBuilder("");
                status = fields.append(doc.get("choice")).toString();

                if(status.equals("Professional teacher / Home tutor")){


                    CollectionReference collectionReference = firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT)
                            .collection("Schedules");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //  List<ScheduleClass> doc = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                            {
                                ScheduleHomeTutorClass scheduleClass = documentSnapshot.toObject(ScheduleHomeTutorClass.class);
//                    System.out.println(scheduleClass.getDay());
                                if(scheduleClass.getDay().equals(dayOfTheWeek) && scheduleClass.getDate().equals(clicked_date))
                                {
                                    // List<ScheduleClass> doc = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                                    scheduleClassList1.add(scheduleClass);
                                    homeAdapter = new HomeHomeTutorAdapter(getContext(),scheduleClassList1);

                                    System.out.println("HIII");
                                    System.out.println(scheduleClassList1);
                                    Log.d("checkDate",scheduleClassList1.toString());

                                    home_recyleview.setVisibility(View.VISIBLE);



                                }
                            }
                            homeAdapter = new HomeHomeTutorAdapter(getContext(),scheduleClassList1);

                            home_recyleview.setAdapter(homeAdapter);
                            homeAdapter.notifyDataSetChanged();

                        }
                    });
                    CollectionReference collectionReference1 =firestore.collection("users")
                            .document(userID).collection("All Files")
                            .document(HT)
                            .collection("Courses");
                    collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            list.clear();
                            List<CourseInfoHomeTutorClass> doc = queryDocumentSnapshots.toObjects(CourseInfoHomeTutorClass.class);
                            list.addAll(doc);
                            for(int i=0;i<list.size();i++)
                            {
                                System.out.println(list.get(i).getCourseName());
                                // System.out.println(list.get(i).getPaymentPerStudent());
                                CollectionReference collectionReference2 = collectionReference1
                                        .document(list.get(i).getCourseName()).collection("Batches");
                                collectionReference2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        list2.clear();
                                        List<BatchClass> doc2 = queryDocumentSnapshots.toObjects(BatchClass.class);
                                        list2.addAll(doc2);
                                        for(int i=0;i<list2.size();i++)
                                        {
                                            money = list2.get(i).getPaymentPerStudent();

                                            System.out.println(list2.get(i).getBatchName());
                                            System.out.println(list2.get(i).getPaymentPerStudent());
                                            CollectionReference collectionReference3 = collectionReference2
                                                    .document(list2.get(i).getBatchName()).collection("Monthly_Payments");
                                            collectionReference3.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    list3.clear();
                                                    List<IncomeClass> doc3 = queryDocumentSnapshots.toObjects(IncomeClass.class);
                                                    list3.addAll(doc3);
                                                    for(int i=0;i<list3.size();i++)
                                                    {
                                                        System.out.println(list3.get(i).getPayment());
                                                        System.out.println(list3.get(i).getId());
                                                        if(list3.get(i).getPayment().equals("paid"))
                                                        {
                                                            payment_counter++;
                                                            paid_sum = paid_sum + money;
                                                            Log.d("checkPaid",paid_sum+"");
                                                            paid.setText(Integer.toString(payment_counter));
                                                        }
                                                        else
                                                        {
                                                            remaining_student++;
                                                            Log.d("checkStu",remaining_student+"");
                                                            unpaid_student.setText(Integer.toString(remaining_student));

                                                        }


                                                    }
                                                    Log.d("checkPaidStudents",payment_counter+"");


                                                    sum = sum +money * list3.size();
                                                    System.out.println(sum);
                                                    expected.setText(Integer.toString(sum));
                                                    Log.d("checkSum",sum+"");
                                                    total.setText(Integer.toString(remaining_student+payment_counter));

                                                    remaining_sum = sum - paid_sum;
                                                    Log.d("checkRem",remaining_sum+"");
                                                    recieved.setText(Integer.toString(paid_sum));
                                                    remaining_money.setText(Integer.toString(remaining_sum));


                                                    //  remaining_student = list3.size()-payment_counter;

                                                }
                                            });

                                        }

                                    }
                                });
                            }

                        }
                    });



                }
                else if(!(status.equals("Professional teacher / Home tutor"))){
                    CollectionReference collectionReference = firestore.collection("users").document(userID)
                            .collection("Schedules");
                    collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            //  List<ScheduleClass> doc = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                            {
                                ScheduleHomeTutorClass scheduleClass = documentSnapshot.toObject(ScheduleHomeTutorClass.class);
//                    System.out.println(scheduleClass.getDay());
                                if(scheduleClass.getDay().equals(dayOfTheWeek) && scheduleClass.getDate().equals(clicked_date))
                                {
                                    // List<ScheduleClass> doc = queryDocumentSnapshots.toObjects(ScheduleClass.class);
                                    scheduleClassList1.add(scheduleClass);
                                    homeAdapter = new HomeHomeTutorAdapter(getContext(),scheduleClassList1);

                                    System.out.println("HIII");
                                    System.out.println(scheduleClassList1);
                                    Log.d("checkDate",scheduleClassList1.toString());

                                    home_recyleview.setVisibility(View.VISIBLE);



                                }
                            }
                            homeAdapter = new HomeHomeTutorAdapter(getContext(),scheduleClassList1);

                            home_recyleview.setAdapter(homeAdapter);
                            homeAdapter.notifyDataSetChanged();

                        }
                    });
                    CollectionReference collectionReference1 = firestore.collection("users").document(userID)
                            .collection("Courses");
                    collectionReference1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            list.clear();
                            List<CourseInfoHomeTutorClass> doc = queryDocumentSnapshots.toObjects(CourseInfoHomeTutorClass.class);
                            list.addAll(doc);
                            for(int i=0;i<list.size();i++)
                            {
                                System.out.println(list.get(i).getCourseName());
                                // System.out.println(list.get(i).getPaymentPerStudent());
                                CollectionReference collectionReference2 = collectionReference1
                                        .document(list.get(i).getCourseName()).collection("Batches");
                                collectionReference2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        list2.clear();
                                        List<BatchClass> doc2 = queryDocumentSnapshots.toObjects(BatchClass.class);
                                        list2.addAll(doc2);
                                        for(int i=0;i<list2.size();i++)
                                        {
                                            money = list2.get(i).getPaymentPerStudent();

                                            System.out.println(list2.get(i).getBatchName());
                                            System.out.println(list2.get(i).getPaymentPerStudent());
                                            CollectionReference collectionReference3 = collectionReference2
                                                    .document(list2.get(i).getBatchName()).collection("Monthly_Payments");
                                            collectionReference3.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    list3.clear();
                                                    List<IncomeClass> doc3 = queryDocumentSnapshots.toObjects(IncomeClass.class);
                                                    list3.addAll(doc3);
                                                    for(int i=0;i<list3.size();i++)
                                                    {
                                                        System.out.println(list3.get(i).getPayment());
                                                        System.out.println(list3.get(i).getId());
                                                        if(list3.get(i).getPayment().equals("paid"))
                                                        {
                                                            payment_counter++;
                                                            paid_sum = paid_sum + money;
                                                            Log.d("checkPaid",paid_sum+"");
                                                            paid.setText(Integer.toString(payment_counter));
                                                        }
                                                        else
                                                        {
                                                            remaining_student++;
                                                            Log.d("checkStu",remaining_student+"");
                                                            unpaid_student.setText(Integer.toString(remaining_student));

                                                        }


                                                    }
                                                    Log.d("checkPaidStudents",payment_counter+"");


                                                    sum = sum +money * list3.size();
                                                    System.out.println(sum);
                                                    expected.setText(Integer.toString(sum));
                                                    Log.d("checkSum",sum+"");
                                                    total.setText(Integer.toString(remaining_student+payment_counter));

                                                    remaining_sum = sum - paid_sum;
                                                    Log.d("checkRem",remaining_sum+"");
                                                    recieved.setText(Integer.toString(paid_sum));
                                                    remaining_money.setText(Integer.toString(remaining_sum));


                                                    //  remaining_student = list3.size()-payment_counter;

                                                }
                                            });

                                        }

                                    }
                                });
                            }

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
}
