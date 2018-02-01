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

/**
 * Created by asif on 27-Dec-17.
 */

class CustomAdapter_Suggestion extends ArrayAdapter<Suggestions>{

    private Context context;
    LayoutInflater inflater;
    static ArrayList<Suggestions> suggestions = new ArrayList<>();

    public CustomAdapter_Suggestion(@NonNull Context context, int resource, ArrayList<Suggestions> suggestions) {
        super(context, resource, suggestions);
        this.context=context;
        inflater = LayoutInflater.from(this.context);
        this.suggestions=suggestions;
        Log.d("save","Click----------------------------2-----------------------------------------------");

    }

    @Override
    public int getCount()
    {
        Log.d("save","size---------------------------------------------------------------------------"+suggestions.size());
        return suggestions.size();
        }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CustomAdapter_Suggestion.MyViewHolder mViewHolder;
        Log.d("save","Click1---------------------------------------------------------------------------");

        if(view==null)
        {
            view= LayoutInflater.from(context).inflate(R.layout.customlayout2,viewGroup,false);
            mViewHolder = new CustomAdapter_Suggestion.MyViewHolder(view);
            view.setTag(mViewHolder);
        }
        else {
            mViewHolder = (CustomAdapter_Suggestion.MyViewHolder) view.getTag();
        }

        Suggestions s =suggestions.get(i);
        mViewHolder.sugg.setText(s.getSuggestion());
        mViewHolder.type.setText(s.getType());

        return view;
    }


    private static class MyViewHolder {
        TextView sugg, type;

        public MyViewHolder(View view) {
            sugg= (TextView) view.findViewById(R.id.suggestion_textview);
            type= (TextView) view.findViewById(R.id.Loc_type_textView);

        }
    }
}
