package com.example.asif.idiot;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by asif on 26-Dec-17.
 */

public class FirebaseHelper_Suggestion {

    static String nn,url;
    static DatabaseReference db;
    Boolean saved;
    static ArrayList<Suggestions> suggestions=new ArrayList<>();

    public DatabaseReference getDb() {
        return db;
    }

    public FirebaseHelper_Suggestion() {
        MainActivity n=new MainActivity();
        nn=n.getName();
        url="https://idiot-ac790.firebaseio.com/"+"Location Based Reminder/"+nn+"/Suggestions";
        db= FirebaseDatabase.getInstance().getReferenceFromUrl(url);

    }

    public ArrayList<Suggestions> getSuggestions() {
        return suggestions;
    }


    public Boolean save(Suggestions suggestions)
    {
        if(suggestions==null)
        {

            saved=false;
        }else
        {
            try
            {
                /*DatabaseReference id = db.push();
                id.child("Time Reminders").setValue(reminder_time);
                */
                db.child("Suggestions").child(suggestions.getType()).setValue(suggestions);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    public void fetchData(DataSnapshot dataSnapshot)
    {
        suggestions.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            System.out.println("In for loop1 Suggestions");

            Suggestions suggestions1=ds.getValue(Suggestions.class);

            System.out.println("In for loop2 vSuggestions");

            suggestions.add(suggestions1);


        }
    }

    //RETRIEVE
    public void retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("In Retrieve Child Added 1 Suggestions");

                fetchData(dataSnapshot);
                System.out.println("In Retrieve Child Added 2 Suggestions");
                System.out.println("In Retrieve Child Added 2 Suggestions SIZE"+suggestions.size());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("In Retrieve child changed 2");

                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("In Retrieve child removed 2");


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                System.out.println("In Retrieve child moved 2");


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("In Retrieve child cancelled 2");

            }



        });
        if (suggestions.size()==0){
            Analayse();
        }
    }

    private void Analayse() {
        String typ[]={"Market","Medical","Grocery"};
        String su[]={"Don't forget the carry bag",
                        "Check the Expiry date",
                        "Check the bill once again"};
        Suggestions s = new  Suggestions();
        for(int i=0;i<=2;i++){
            s.setSuggestion(su[i]);
            s.setType(typ[i]);
            save(s);
        }

    }


}
