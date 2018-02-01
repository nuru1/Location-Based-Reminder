package com.example.asif.idiot;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {

    FirebaseHelper_Location helper;
    ListView LocationList;
    CustomAdapter_Location customAdapter;
    static ArrayList saved_location = new ArrayList();


    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_location, container, false);
        helper = new FirebaseHelper_Location();
        saved_location=helper.getReminders_locations();
        LocationList = (ListView) v.findViewById(R.id.ListView2);
        Log.d("In fragment", "Custom adapter---------kkkkkk");
        System.out.println("In Fragment");
        customAdapter=new CustomAdapter_Location(container.getContext(),android.R.layout.simple_list_item_activated_1,saved_location);
        customAdapter.notifyDataSetChanged();
        LocationList.setAdapter(customAdapter);
        return v;
    }

}
