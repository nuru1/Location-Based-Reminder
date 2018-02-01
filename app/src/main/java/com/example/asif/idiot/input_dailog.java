package com.example.asif.idiot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by asif on 27-Dec-17.
 */

public class input_dailog extends DialogFragment {
    EditText t,s;
    Button save;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.input_dailog,null);
        getDialog().setTitle("Save Suggestion:");
        save = (Button) v.findViewById(R.id.saveBtn);
        t = (EditText) v.findViewById(R.id.SuggEditText);
        s = (EditText) v.findViewById(R.id.TypeEditText);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("Clcik------------------------------------------------------------------------------------");
                String typ = t.getText().toString();
                String suggst = s.getText().toString();
                if(typ!=null && suggst!=null) {
                    Suggestions s = new Suggestions();
                    s.setType(suggst);
                    s.setSuggestion(typ);
                    FirebaseHelper_Suggestion helper = new FirebaseHelper_Suggestion();
                    helper.save(s);
                    getDialog().dismiss();
                }
            }
        });
        return v;
    }
}
