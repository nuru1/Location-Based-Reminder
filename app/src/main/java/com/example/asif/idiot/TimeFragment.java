package com.example.asif.idiot;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.ArrayList;

public class TimeFragment extends Fragment {

    FirebaseHelper helper;
    ListView ReminderList;
    CustomAdapter customAdapter;
    static ArrayList saved_reminders = new ArrayList();

    public TimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_time, container, false);
        helper = new FirebaseHelper();
        saved_reminders = helper.getReminders();
        ReminderList = (ListView) v.findViewById(R.id.ListView);
        Log.d("In fragment", "Custom adapter---------kkkkkk");
        System.out.println("In Fragment");
        customAdapter = new CustomAdapter(container.getContext(), android.R.layout.simple_list_item_activated_1, saved_reminders);
        customAdapter.notifyDataSetChanged();
        ReminderList.setAdapter(customAdapter);
        ReminderList.setVisibility(v.VISIBLE);
        return v;
    }


}