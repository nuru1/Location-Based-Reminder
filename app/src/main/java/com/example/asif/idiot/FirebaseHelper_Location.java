package com.example.asif.idiot;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by asif on 19-Dec-17.
 */

public class FirebaseHelper_Location {


    static String nn,url;
    static DatabaseReference db;
    Boolean saved;
    static ArrayList<Reminder_Location> reminders_locations=new ArrayList<>();

    public DatabaseReference getDb() {
        return db;
    }

    public FirebaseHelper_Location() {
        MainActivity n=new MainActivity();
        nn=n.getName();
      //  url="https://idiot-ac790.firebaseio.com/"+nn+"/";
        db= FirebaseDatabase.getInstance().getReference("Location Based Reminder/"+nn+"/Location");
    }

    public ArrayList<Reminder_Location> getReminders_locations() {
        return reminders_locations;
    }


    public Boolean save_location(Reminder_Location reminder_location)
    {
        String id=reminder_location.getId();
        if(reminder_location==null)
        {

            saved=false;
        }else
        {
            try
            {

                db.child("Location").child(id).setValue(reminder_location);
                saved=true;
            }
            catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }

        return saved;
    }

    public void fetchData_loc(DataSnapshot dataSnapshot)
    {
        reminders_locations.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            System.out.println("In for loop1 Location");

            Reminder_Location reminder_location=ds.getValue(Reminder_Location.class);

            System.out.println("In for loop2 Location");

            reminders_locations.add(reminder_location);

            System.out.println("In for loop3   ");

        }
    }

    //RETRIEVE
    public void retrieve_loc()
    {
        //db= FirebaseDatabase.getInstance().getReferenceFromUrl(url+"/Location Reminder");
        System.out.println("In Retrieve");

        // Read from the database

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("In Retrieve Child Added 1 Location");

                fetchData_loc(dataSnapshot);
                System.out.println("In Retrieve Child Added 2 Location");

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println("In Retrieve child changed 2 Location");

                fetchData_loc(dataSnapshot);

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
        System.out.println("OUT!!");
        if (reminders_locations.size()!=0)
        AnalyseData();
    }

    private void AnalyseData() {
        Reminder_Location locs;
        Calendar calendar=Calendar.getInstance();
        int sett,dOw = calendar.get(calendar.DAY_OF_WEEK);
        for(int i=0; i<reminders_locations.size();i++){
            locs=reminders_locations.get(i);
            sett=locs.getSetter();
            if(sett!= 0 && dOw!=sett ){
                locs.setSetter(0);
                save_location(locs);
            }
        }
    }


}
