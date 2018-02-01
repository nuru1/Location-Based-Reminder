package com.example.asif.idiot;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Fragment_Reminders extends Fragment implements View.OnClickListener {



    private FragmentActivity myContext;
    Fragment TimeFragment,LocationFragment;
    static Button TimeButton,LocationButton;

    public Fragment_Reminders() {
        // Required empty public construc
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_fragment__reminders, container, false);

        LocationButton=v.findViewById(R.id.location);
        TimeButton=v.findViewById(R.id.time);

        LocationFragment = new LocationFragment();
        TimeFragment = new TimeFragment();
        TimeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
        FragmentManager fragmentManager= myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_reminders, LocationFragment).commit();

        TimeButton.setOnClickListener(this);
        LocationButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        TimeButton.setBackgroundColor(Color.parseColor("#1E88E5"));
        LocationButton.setBackgroundColor(Color.parseColor("#1E88E5"));
        FragmentManager fragmentManager= myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()){
            case R.id.time:
                TimeButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                fragmentTransaction.replace(R.id.frame_reminders, TimeFragment).commit();
                break;
            case R.id.location:
                LocationButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                fragmentTransaction.replace(R.id.frame_reminders, LocationFragment).commit();
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

}





























/*

public class Fragment_Reminders extends Fragment {


    FirebaseHelper helper;
    static ListView ReminderList;
    Switch sw;
    CustomAdapter customAdapter;
    ArrayList<Reminder_Time> saved_reminders = new ArrayList<>();

    public Fragment_Reminders() {
        // Required empty public construc
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View  v = inflater.inflate(R.layout.fragment_fragment__reminders, container, false);

        ReminderList = (ListView) v.findViewById(R.id.ListView);
        helper = new FirebaseHelper();

        if(savedInstanceState==null){
            Log.d("In fragment", "Custom adapter---------kkkkkk");
            System.out.println("In Fragment");
            setAdapter();
            System.out.println("In Fragment under custum adapter");

        }
        else {
            customAdapter=new CustomAdapter(getContext(),saved_reminders);
            ReminderList.setAdapter(customAdapter);
            System.out.print("In saved instance");
        }
        return v;
    }


    public void setAdapter() {

        System.out.println("In setadapter");
        helper = new FirebaseHelper();
        customAdapter  = new CustomAdapter(getContext(),helper.retrieve());
        ReminderList.setAdapter(customAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        helper = new FirebaseHelper();
        saved_reminders = helper.retrieve();
    }
}













  */
/*  public class CustomAdapter extends BaseAdapter {
        int status;
        Context c;
        ArrayList<Reminder_Time> reminders;

        public CustomAdapter(ArrayList<Reminder_Time> reminders) {
            this.c = c;
            this.reminders = reminders;
        }

        @Override
        public int getCount() { return reminders.size(); }

        @Override
        public Object getItem(int position) { return reminders.get(position); }

        @Override
        public long getItemId(int position) { return position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null || getCount()==0)
            {
                convertView= LayoutInflater.from(c).inflate(R.layout.customlayout,parent,false);
            }

            TextView Time= (TextView) convertView.findViewById(R.id.time_dsply);
            TextView Date= (TextView) convertView.findViewById(R.id.date_dsply);
            TextView message= (TextView) convertView.findViewById(R.id.message_dsply);
            Switch aSwitch = (Switch) convertView.findViewById(R.id.switch_reminder);
            final Reminder_Time s= (Reminder_Time) this.getItem(position);

            String date= (s.getS_date()+" : "+s.getS_month()+" : "+s.getS_year());
            String time=(s.getS_hour()+" : "+s.getS_min());
            status=s.getS_Value();
            Time.setText(time);
            Date.setText(date);
            message.setText(s.getS_message());
            if(status==1){
                aSwitch.setChecked(true);
            }

            //ONITECLICK
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toast.makeText(c,s.getS_hour(),Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }
    }

*//*


*/
