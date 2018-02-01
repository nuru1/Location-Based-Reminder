package com.example.asif.idiot;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class CustomAdapter_Location extends ArrayAdapter<Reminder_Location> {

    private static List<Reminder_Location> loc = new ArrayList();
    int status;
    private Context ctx;
    LayoutInflater inflater;

    String[] Days = new String[]{
            "Once",
            "Daily",
            "Every Monday",
            "Every Tuesday",
            "Every Wednesday",
            "Every Thursday",
            "Every Friday",
            "Every Saturday",
            "Every Sunday"
    };



    public CustomAdapter_Location(@NonNull Context ctx,int resource, ArrayList<Reminder_Location> loc) {
        super(ctx,resource ,loc);
        this.loc=loc;
        this.ctx=ctx;
        inflater = LayoutInflater.from(this.ctx);
        System.out.println("Setting adapter Out of Retrieve Location");
    }

    @Override
    public int getCount() {
        System.out.println("Location count: "+loc.size());
        return loc.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //view = getLayoutInflater().inflate(R.layout.customlayout, null);
        System.out.println("In view" + "");

        CustomAdapter_Location.MyViewHolder mViewHolder;

        if(view==null)
        {
            view= LayoutInflater.from(ctx).inflate(R.layout.customlayout_location,viewGroup,false);
            mViewHolder = new CustomAdapter_Location.MyViewHolder(view);
            view.setTag(mViewHolder);
        }
        else {
            mViewHolder = (CustomAdapter_Location.MyViewHolder) view.getTag();
        }

        Reminder_Location r =loc.get(i);
        status = r.getStatus();
        mViewHolder.message.setText(r.getMessage());
        mViewHolder.location.setText(r.getLocation());
        mViewHolder.latlng.setText(r.getLattitude()+ " , "+r.getLongitude());
        mViewHolder.repeat.setText(Days[r.getRepeat()]);
        if (status == 1) {
            mViewHolder.sw.setChecked(true);
        }

        System.out.println(" Set !!");
        Log.d("Context========",ctx+"     +++++++++++++++");

        return view;
    }


    private static class MyViewHolder {
        TextView location,repeat,message,latlng;
        Switch sw;

        public MyViewHolder(View view) {
            message = (TextView) view.findViewById(R.id.meassage_loc_dsply);
            location = (TextView) view.findViewById(R.id.location_dsply);
            latlng = (TextView) view.findViewById(R.id.latng_dsply);
            repeat = (TextView) view.findViewById(R.id.repeat_dsply);
            sw = (Switch) view.findViewById(R.id.switch_location);
        }
    }
}
