package com.example.chalkboard_copy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ChatsFragment_HomeTutor extends Fragment {
    Toolbar chats_toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String userID = firebaseAuth.getCurrentUser().getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_hometutor,container, false);
        tabLayout = view.findViewById(R.id.table_layout_ht);
        viewPager = view.findViewById(R.id.my_viewpager_ht);
        //   ((AppCompatActivity)getActivity()).setSupportActionBar(chats_toolbar);
        //  ((Features) getActivity()).getSupportActionBar();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        return view;
    }
    public static ChatsFragment newInstance() {
        ChatsFragment fragment = new ChatsFragment();


        return fragment;
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new MessagesFragmentHometutor(),"Messages");
        viewPagerAdapter.addFragment(new UsersFragmentHomeTutor(),"Users");
        viewPager.setAdapter(viewPagerAdapter);

    }
    private void status(String status)
    {
        DocumentReference documentReference = firestore.collection("users").document(userID);
        Map<String,Object> user = new HashMap<>();
        user.put("active_status",status);
        documentReference.set(user, SetOptions.merge());

    }

    @Override
    public void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        status("offline");
    }
}
