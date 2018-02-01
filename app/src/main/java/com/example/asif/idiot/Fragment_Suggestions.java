package com.example.asif.idiot;


import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Suggestions extends Fragment {

    ViewGroup viewGroup;
    ListView suggestions_listview;
    View v;
    Context ctx;
    static ArrayList<Suggestions> suggestions = new ArrayList<>();
    static String Loc_type[]={
            "Null",
            "Market",
            "Medical",
            "Forget"
    };

    public Fragment_Suggestions() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("frag sug","size before "+suggestions.size());
        System.out.print("frag sug size before "+suggestions.size());
        viewGroup = container;
         v =inflater.inflate(R.layout.fragment_fragment__suggestions, container, false);
        FirebaseHelper_Suggestion helper_suggestion = new FirebaseHelper_Suggestion();
        suggestions=helper_suggestion.getSuggestions();
        Log.d("frag sug","size after "+suggestions.size());
         FloatingActionButton floatingActionButton= (FloatingActionButton) v.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_dialog(view);
            }
        });

        suggestions_listview = (ListView)v.findViewById(R.id.ListView_suggestions);
        CustomAdapter_Suggestion customAdapter = new CustomAdapter_Suggestion(container.getContext(),android.R.layout.simple_list_item_activated_1,suggestions);
        customAdapter.notifyDataSetChanged();
        suggestions_listview.setAdapter(customAdapter);

        return v;
    }

    private void set_dialog(View v) {
        input_dailog dailog = new input_dailog();
        dailog.setStyle(DialogFragment.STYLE_NORMAL,R.style.CustomDialog);
        dailog.show(getFragmentManager(),"Save Suggestion");
    }


    public String[] getLoc_type() {
        return Loc_type;
    }


}


