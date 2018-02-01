package com.example.asif.idiot;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class CustomAdapter extends ArrayAdapter<Reminder_Time> {

    private List<Reminder_Time> rem = new ArrayList();
    int status;
    private Context ctx;
    LayoutInflater inflater;


    public CustomAdapter(@NonNull Context ctx,int resource, ArrayList<Reminder_Time> rem) {
        super(ctx,resource ,rem);
        this.rem=rem;
        this.ctx=ctx;
        inflater = LayoutInflater.from(this.ctx);
        System.out.println("Setting adapter Out of Retrieve");
    }

    @Override
    public int getCount() {
        System.out.println("Count:  Size  "+rem.size());
        //  System.out.println(" val "+rem.get(0).getS_date());

        return rem.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //view = getLayoutInflater().inflate(R.layout.customlayout, null);
      System.out.println("In view" + "");

        MyViewHolder mViewHolder;

        if(view==null)
        {
            view= LayoutInflater.from(ctx).inflate(R.layout.customlayout,viewGroup,false);
            mViewHolder = new MyViewHolder(view);
            view.setTag(mViewHolder);
        }
        else {
            mViewHolder = (MyViewHolder) view.getTag();
        }

        Reminder_Time s =rem.get(i);
        String dat = (s.getS_date() + " : " + s.getS_month() + " : " + s.getS_year());
        String tim = (s.getS_hour() + " : " + s.getS_min());
        status = s.getS_Value();
        mViewHolder.Time.setText(tim);
        mViewHolder.Date.setText(dat);
        mViewHolder.Message.setText(s.getS_message());
        if (status == 1) {
            mViewHolder.sw.setChecked(true);
        }

        System.out.println(" Set !!");
        Log.d("Context========",ctx+"     +++++++++++++++");

        return view;
    }


    private static class MyViewHolder {
        TextView Time, Date, Message;
        Switch sw;

        public MyViewHolder(View view) {
            Time = (TextView) view.findViewById(R.id.time_dsply);
            Date = (TextView) view.findViewById(R.id.date_dsply);
            Message = (TextView) view.findViewById(R.id.message_dsply);
            sw = (Switch) view.findViewById(R.id.switch_reminder);
        }
    }

}
