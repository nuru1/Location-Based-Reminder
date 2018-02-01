package com.example.asif.idiot;


import android.util.Log;
import android.widget.Toast;

import com.example.asif.idiot.Alarm.SetAlarm;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.os.Build.ID;

/**
 * Created by Oclemy on 6/21/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 * 1.SAVE DATA TO FIREBASE
 * 2. RETRIEVE
 * 3.RETURN AN ARRAYLIST
 */
public class FirebaseHelper {

    static String nn,url;
    static DatabaseReference db;
    Boolean saved;
    static ArrayList<Reminder_Time> reminders=new ArrayList<>();

    public DatabaseReference getDb() {
        return db;
    }

    public FirebaseHelper() {
        MainActivity n=new MainActivity();
        nn=n.getName();
//        url="https://idiot-ac790.firebaseio.com/"+nn+"/";
        db= FirebaseDatabase.getInstance().getReference("Location Based Reminder/"+"Time Reminders");

    }

    public ArrayList<Reminder_Time> getReminders() {
        return reminders;
    }


    public Boolean save(Reminder_Time reminder_time)
    {
        if(reminder_time==null)
        {

            saved=false;
        }else
        {
            try
            {
                String Id=reminder_time.getId();
                db.child(Id).setValue(reminder_time);
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
        reminders.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            System.out.println("In for loop1");

            Reminder_Time reminders_time=ds.getValue(Reminder_Time.class);

            System.out.println("In for loop2");

            reminders.add(reminders_time);

            System.out.println("In for loop3   "+reminders.get(0).getS_date());
            System.out.println("In size"+reminders.size());

        }
    }

    //RETRIEVE
    public void retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("In Retrieve Child Added 1");

                fetchData(dataSnapshot);
                System.out.println("In Retrieve Child Added 2");

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

        /*if(reminders.size()!=0) {
            SetAlarm set = new SetAlarm(reminders);
            set.AddAlarms();
        }*/
    }







}


